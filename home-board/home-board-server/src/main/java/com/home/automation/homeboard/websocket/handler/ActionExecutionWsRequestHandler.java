package com.home.automation.homeboard.websocket.handler;

import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.service.model.ActionService;
import com.home.automation.homeboard.websocket.mediator.SubscriberRegistry;
import com.home.automation.homeboard.websocket.message.MessageType;
import com.home.automation.homeboard.websocket.message.request.PioRequest;
import com.home.automation.homeboard.websocket.message.request.StompRequest;
import com.home.automation.homeboard.ws.ActionMessagePayload;
import com.home.automation.homeboard.ws.NonSubscribedBoardMessagePayload;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;


public class ActionExecutionWsRequestHandler extends AbstractWsRequestHandler<StompRequest>
{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
    private ActionService actionService;

	private SubscriberRegistry subscriberRegistry;

	private String commandDestination;

	@Override
	public boolean canHandle(final StompRequest wsMessage)
	{
		if (!MessageType.SEND.equals(wsMessage.getMessageType())) {
			return false;
		}

		if (!ActionMessagePayload.class.equals(wsMessage.getPayload().getType())) {
			return false;
		}

		return wsMessage.getDestination()
				.filter(s -> StringUtils.equals(commandDestination, s))
				.isPresent();
	}

	@Override
	protected void handleInternal(final StompRequest request)
	{
		final ActionMessagePayload cmp = (ActionMessagePayload)request.getPayload();
		final String executorId = cmp.getExecutorId();

		// TODO: add custom exceptions
		if (StringUtils.isBlank(executorId)) {
			throw new IllegalStateException("Executor id must be supplied.");
		}
		final ActionModel action = actionService.findExecutorById(executorId).get();
		if (BooleanUtils.isNotTrue(action.getActive())) {
			throw new IllegalStateException(String.format("Action with id %s is not active.", executorId));
		}
		if (action.getBoard() == null) {
			throw new IllegalStateException(String.format("Action with id %s doesn't have a board attached.", executorId));
		}

		final String messageId = request.getMessageId().get();
		boolean isBoardSubscribed = subscriberRegistry.isMicroControllerBoardSubscribed(action.getBoard().getExternalBoardId());
		if (isBoardSubscribed) {
			getMessageMediator().handleBoardMessages(createBoardPIORequest(action, cmp, messageId));

		} else {
			final StompRequest stompRequest = createBoardStompRequest(action, cmp, messageId);
			stompRequest.setInitiatingSubscriber(request.getInitiatingSubscriber());
			getMessageMediator().sendMessageBackToInitiatingSubscriber(stompRequest);
		}
	}

	private PioRequest createBoardPIORequest(final ActionModel action, ActionMessagePayload commandRequest, String messageId) {
		final PioRequest pio = new PioRequest();
		pio.setBoardId(action.getBoard().getExternalBoardId());
		pio.setMessageId(messageId);
		pio.setPayload(String.valueOf(commandRequest.getPayload()));
		pio.setExecutorId(action.getExecutorId());
		pio.setMessageType(MessageType.MESSAGE);
		return pio;
	}

	private StompRequest createBoardStompRequest(final ActionModel action, final ActionMessagePayload cmp, final String messageId) {
		final NonSubscribedBoardMessagePayload bmp = new NonSubscribedBoardMessagePayload();
		bmp.setBoardName(action.getBoard().getName());
		bmp.setActionIds(Arrays.asList(String.valueOf(action.getId())));
		bmp.setPayload(cmp.getPayload());

		final StompRequest stompRequest = new StompRequest();
		stompRequest.setMessageId(messageId);
		stompRequest.setMessageType(MessageType.MESSAGE);
		stompRequest.setPayload(bmp);
		stompRequest.setContentType("application/json");
		return stompRequest;
	}

	public void setCommandDestination(String commandDestination)
	{
		this.commandDestination = commandDestination;
	}

	public void setSubscriberRegistry(SubscriberRegistry subscriberRegistry) {
		this.subscriberRegistry = subscriberRegistry;
	}


	// TODO: muta ce e mai jos intr-un alt handler, care sa gestioneze o comanda (mai multe actiuni in acelasi timp)
//	private void splitTest() {
		//		final List<BoardActionExecution> boardExecution = createBoardActionExecutionFromCommand(actionId);
//		collectAndExecuteBoardAction(boardExecution,
//				(baes) -> { // subscribed baes
//					baes.stream().map(bae -> createBoardPIORequest(bae, cmp, messageId))
//							.forEach(getMessageMediator()::handleBoardMessages);
//				}, (baes) -> { // unsubscribed baes
//					baes.stream().map(bae -> createBoardStompRequest(bae, cmp, messageId))
//							.forEach(stompRequest -> request.getInitiatingSubscriber().sendMessage(stompRequest));
//				});
//	}

	//	private List<BoardActionExecution> createBoardActionExecutionFromCommand(final String actionId) {
//
//		final MultiValueMap<BoardModel, ActionModel> boardActionsMap = commandService.splitActionsPerBoardFromCommand(Long.valueOf(actionId));
//
//		return boardActionsMap.entrySet().stream()
//				.map(this::createBoardActionExecution)
//				.filter(bae -> {
//					if (bae.getBoardModel() == null) {
//						logger.info(String.format("No board found in one of the actions of command %s"), actionId);
//						return false;
//					}
//					return true;
//				})
//				.collect(Collectors.toList());
//	}

//	private void collectAndExecuteBoardAction(final List<BoardActionExecution> baes,
//											  final Consumer<List<BoardActionExecution>> subscribedExecutor,
//											  final Consumer<List<BoardActionExecution>> unsubscribedExecutor) {
//
//		final Map<Boolean, List<BoardActionExecution>> baesPartitioning = baes.stream()
//				.collect(Collectors.partitioningBy(bae -> subscriberRegistry.isMicroControllerBoardSubscribed(bae.getBoardModel().getExternalBoardId())));
//
//		if (baesPartitioning.containsKey(Boolean.FALSE)) {
//			unsubscribedExecutor.accept(baesPartitioning.get(Boolean.FALSE));
//		}
//		if (baesPartitioning.containsKey(Boolean.TRUE)) {
//			subscribedExecutor.accept(baesPartitioning.get(Boolean.TRUE));
//		}
//	}

//	private BoardActionExecution createBoardActionExecution(Map.Entry<BoardModel, List<ActionModel>> boardModelListEntry) {
//		final BoardActionExecution bae = new BoardActionExecution();
//		bae.setBoardModel(boardModelListEntry.getKey());
//		bae.setActionIds(boardModelListEntry.getValue());
//		return bae;
//	}
//
//	private PioRequest createBoardPIORequest(BoardActionExecution bae, ActionMessagePayload commandRequest, String messageId) {
//		final PioRequest pio = new PioRequest();
//		pio.setBoardId(bae.getBoardModel().getExternalBoardId());
//		pio.setMessageId(messageId);
//		CollectionUtils.emptyIfNull(bae.getActionIds()).stream()
//				.filter(am -> BooleanUtils.isTrue(am.getActive()))
//				.map(am -> convertActionToString(am, commandRequest))
//				.forEach(pio::addCommand);
//		return pio;
//	}

	//	private StompRequest createBoardStompRequest(BoardActionExecution bae, ActionMessagePayload commandRequest, String messageId) {
//		final NonSubscribedBoardMessagePayload bmp = new NonSubscribedBoardMessagePayload();
//		bmp.setBoardName(bae.getBoardModel().getName());
//		bmp.setActionIds(bae.getActionIds().stream().map(ActionModel::getName).collect(Collectors.toList()));
//
//		final StompRequest stompRequest = new StompRequest();
//		stompRequest.setMessageId(messageId);
//		stompRequest.setMessageType(MessageType.MESSAGE);
//		stompRequest.setPayload(bmp);
//		stompRequest.setContentType("application/json");
//		return stompRequest;
//	}
//
//	private String convertActionToString(ActionModel actionModel, ActionMessagePayload commandRequest) {
//		return actionModel.getCommand();
//	}
//	private class BoardActionExecution {
//		BoardModel boardModel;
//		List<ActionModel> actions;
//
//		BoardModel getBoardModel() {
//			return boardModel;
//		}
//
//		void setBoardModel(BoardModel boardModel) {
//			this.boardModel = boardModel;
//		}
//
//		List<ActionModel> getActionIds() {
//			return actions;
//		}
//
//		void setActionIds(List<ActionModel> actions) {
//			this.actions = actions;
//		}
//	}
}
