package com.home.automation.homeboard.websocket.handler;

import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.domain.BoardModel;
import com.home.automation.homeboard.service.model.CommandService;
import com.home.automation.homeboard.websocket.mediator.SubscriberRegistry;
import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.PioRequest;
import com.home.automation.homeboard.websocket.message.request.StompRequest;
import com.home.automation.homeboard.ws.CommandMessagePayload;
import com.home.automation.homeboard.ws.NonSubscribedBoardMessagePayload;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class CommandExecutionWsRequestHandler extends AbstractWsRequestHandler<StompRequest>
{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
    private CommandService commandService;

	private SubscriberRegistry subscriberRegistry;

	private String commandDestination;

	@Override
	public boolean canHandle(final StompRequest wsMessage)
	{
		if (!MessageType.SEND.equals(wsMessage.getMessageType())) {
			return false;
		}

		if (!CommandMessagePayload.class.equals(wsMessage.getPayload().getType())) {
			return false;
		}

		return wsMessage.getDestination()
				.filter(s -> StringUtils.equals(commandDestination, s))
				.isPresent();
	}

	@Override
	protected void handleInternal(final StompRequest request)
	{
		final CommandMessagePayload cmp = (CommandMessagePayload)request.getPayload();
		if (StringUtils.isBlank(cmp.getCommandId())) {
			throw new IllegalStateException("Command id must be supplied.");
		}

		final String commandId = cmp.getCommandId();
		final String messageId = request.getMessageId().get();

		final List<BoardActionExecution> boardExecution = createBoardActionExecutionFromCommand(commandId);
		collectAndExecuteBoardAction(boardExecution,
				(baes) -> { // subscribed baes
					baes.stream().map(bae -> createBoardPIORequest(bae, cmp, messageId))
							.forEach(getMessageMediator()::handleBoardMessages);
				}, (baes) -> { // unsubscribed baes
					baes.stream().map(bae -> createBoardStompRequest(bae, cmp, messageId))
							.forEach(stompRequest -> request.getInitiatingSubscriber().sendMessage(stompRequest));
				});

	}

	private List<BoardActionExecution> createBoardActionExecutionFromCommand(final String commandId) {
		final MultiValueMap<BoardModel, ActionModel> boardActionsMap =
				commandService.splitActionsPerBoardFromCommand(Long.valueOf(commandId));
		return boardActionsMap.entrySet().stream()
				.map(this::createBoardActionExecution)
				.filter(bae -> {
					if (bae.getBoardModel() == null) {
						logger.info(String.format("No board found in one of the actions of command %s"), commandId);
						return false;
					}
					return true;
				})
				.collect(Collectors.toList());
	}

	private void collectAndExecuteBoardAction(final List<BoardActionExecution> baes,
											  final Consumer<List<BoardActionExecution>> subscribedExecutor,
											  final Consumer<List<BoardActionExecution>> unsubscribedExecutor) {

		final Map<Boolean, List<BoardActionExecution>> baesPartitioning = baes.stream()
				.collect(Collectors.partitioningBy(bae -> subscriberRegistry.isMicroControllerBoardSubscribed(bae.getBoardModel().getExternalBoardId())));

		if (baesPartitioning.containsKey(Boolean.FALSE)) {
			unsubscribedExecutor.accept(baesPartitioning.get(Boolean.FALSE));
		}
		if (baesPartitioning.containsKey(Boolean.TRUE)) {
			subscribedExecutor.accept(baesPartitioning.get(Boolean.TRUE));
		}
	}

	private BoardActionExecution createBoardActionExecution(Map.Entry<BoardModel, List<ActionModel>> boardModelListEntry) {
		final BoardActionExecution bae = new BoardActionExecution();
		bae.setBoardModel(boardModelListEntry.getKey());
		bae.setActions(boardModelListEntry.getValue());
		return bae;
	}

	private PioRequest createBoardPIORequest(BoardActionExecution bae, CommandMessagePayload commandRequest, String messageId) {
		final PioRequest pio = new PioRequest();
		pio.setBoardId(bae.getBoardModel().getExternalBoardId());
		pio.setMessageId(messageId);
		CollectionUtils.emptyIfNull(bae.getActions()).stream()
				.filter(am -> BooleanUtils.isTrue(am.getActive()))
				.map(am -> convertActionToString(am, commandRequest))
				.forEach(pio::addCommand);
		return pio;
	}

	private StompRequest createBoardStompRequest(BoardActionExecution bae, CommandMessagePayload commandRequest, String messageId) {
		final NonSubscribedBoardMessagePayload bmp = new NonSubscribedBoardMessagePayload();
		bmp.setBoardName(bae.getBoardModel().getName());
		bmp.setActions(bae.getActions().stream().map(ActionModel::getName).collect(Collectors.toList()));

		final StompRequest stompRequest = new StompRequest();
		stompRequest.setMessageId(messageId);
		stompRequest.setMessageType(MessageType.MESSAGE);
		stompRequest.setPayload(bmp);
		stompRequest.setContentType("application/json");
		return stompRequest;
	}

	private String convertActionToString(ActionModel actionModel, CommandMessagePayload commandRequest) {
		return actionModel.getCommand();
	}

	public void setCommandDestination(String commandDestination)
	{
		this.commandDestination = commandDestination;
	}

	public void setSubscriberRegistry(SubscriberRegistry subscriberRegistry) {
		this.subscriberRegistry = subscriberRegistry;
	}

	private class BoardActionExecution {
		BoardModel boardModel;
		List<ActionModel> actions;

		BoardModel getBoardModel() {
			return boardModel;
		}

		void setBoardModel(BoardModel boardModel) {
			this.boardModel = boardModel;
		}

		List<ActionModel> getActions() {
			return actions;
		}

		void setActions(List<ActionModel> actions) {
			this.actions = actions;
		}
	}
}
