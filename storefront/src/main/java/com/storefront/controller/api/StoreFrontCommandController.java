package com.storefront.controller.api;

import com.home.automation.homeboard.client.BoardClient;
import com.home.automation.homeboard.data.ActionData;
import com.home.automation.homeboard.data.CommandDataList;
import com.storefront.data.FEActionData;
import com.storefront.data.FERoomActionData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class StoreFrontCommandController {

    @Autowired
    private BoardClient boardClient;

    @GetMapping("/api/rooms")
    public List<FERoomActionData> getCommandsPerRooms(@RequestParam(value = "un", required = false) String userName,
                                                      @RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        final ResponseEntity<CommandDataList> activeCommandsByCount = boardClient.findActiveCommandsByCount(count);
        // TODO: treat non 200 responses and exceptions
        // TODO: refactor.. move convert in alta parte, etc
        final CommandDataList commandDataList = activeCommandsByCount.getBody();
        return convertToFeRoomCommandData(commandDataList, count);
    }

    private List<FERoomActionData> convertToFeRoomCommandData(CommandDataList commandDataList, Integer count) {
        final MultiValuedMap<String, ActionData> roomCommands = createRoomCommandMap(commandDataList);

        return roomCommands.asMap().entrySet().stream()
                .map(this::convertToRoomCommand)
                .collect(Collectors.toList());
    }

    private MultiValuedMap<String, ActionData> createRoomCommandMap(CommandDataList commandDataList) {
        final MultiValuedMap<String, ActionData> roomCommands = new HashSetValuedHashMap<>();
        CollectionUtils.emptyIfNull(commandDataList.getCommandDataList()).stream()
                .flatMap(commandData -> commandData.getActions().stream())
                .forEach(actionData -> {
                    roomCommands.put(actionData.getRoom(), actionData);
                });
        return roomCommands;
    }

    private FERoomActionData convertToRoomCommand(Map.Entry<String, Collection<ActionData>> entry) {
        final FERoomActionData roomData = new FERoomActionData();
        roomData.setRoom(entry.getKey());
        final List<FEActionData> feCommands = entry.getValue().stream()
                .map(this::convertToFEAction).collect(Collectors.toList());
        roomData.setActions(feCommands);
        return roomData;
    }

    private FEActionData convertToFEAction(final ActionData actionData) {
        final FEActionData feActionData = new FEActionData();
        feActionData.setCommand(actionData.getCommand());
        feActionData.setId(String.valueOf(actionData.getId()));
        feActionData.setName(actionData.getName());
        feActionData.setParentCommandName(actionData.getParentCommandName());
        feActionData.setActionType(actionData.getActionType());
        feActionData.setExecutorId(actionData.getExecutorId());
        return feActionData;
    }
}
