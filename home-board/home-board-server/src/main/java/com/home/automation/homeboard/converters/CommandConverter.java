package com.home.automation.homeboard.converters;

import com.home.automation.homeboard.converters.base.AbstractBaseConverter;
import com.home.automation.homeboard.data.ActionData;
import com.home.automation.homeboard.data.CommandData;
import com.home.automation.homeboard.domain.ActionModel;
import com.home.automation.homeboard.domain.CommandModel;
import com.home.automation.homeboard.domain.associations.CommandActionModel;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CommandConverter extends AbstractBaseConverter<CommandModel, CommandData> {

    public CommandConverter() {
        super(CommandModel.class, CommandData.class);
    }

    protected void init() {
        setConverter(createCommandBidirectionalConverter());
        super.init();
    }

    private BidirectionalConverter<CommandData, CommandModel> createCommandBidirectionalConverter() {
        return new BidirectionalConverter<CommandData, CommandModel>() {

            @Override
            public CommandModel convertTo(CommandData commandData, Type<CommandModel> type, MappingContext mappingContext) {
                final CommandModel cm = new CommandModel();
                cm.setDescription(commandData.getDescription());
                cm.setName(commandData.getName());
                cm.setActive(commandData.isActive());
                CollectionUtils.emptyIfNull(commandData.getActions()).stream()
                        .map(CommandConverter.this::createActionModel)
                        .forEach(action -> cm.addAction(action, false));

                return cm;
            }

            @Override
            public CommandData convertFrom(CommandModel commandActionModel, Type<CommandData> type, MappingContext mappingContext) {
                final CommandData cd = new CommandData();

                cd.setName(commandActionModel.getName());
                cd.setDescription(commandActionModel.getDescription());
                cd.setDateCreated(commandActionModel.getDateCreated());
                cd.setDateModified(commandActionModel.getDateModified());
                cd.setId(commandActionModel.getId());
                cd.setActions(CollectionUtils.emptyIfNull(commandActionModel.getActions()).stream().map(CommandConverter.this::createActionData).collect(Collectors.toSet()));
                return cd;
            }
        };
    }

    private ActionData createActionData(CommandActionModel commandActionModel) {
        final ActionModel action = commandActionModel.getAction();

        final ActionData ad = new ActionData();
        ad.setName(action.getName());
        ad.setCommand(action.getCommand());
        ad.setDescription(action.getDescription());
        ad.setDateCreated(action.getDateCreated());
        ad.setDateModified(action.getDateModified());
        ad.setId(action.getId());
//        ad.setBoards(createBoardData());
        return ad;
    }

    private ActionModel createActionModel(ActionData actionData) {
        final ActionModel am = new ActionModel();
        am.setDescription(actionData.getDescription());
        am.setName(actionData.getName());
        am.setCommand(actionData.getCommand());
        return am;
    }

}
