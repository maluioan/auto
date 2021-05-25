package com.home.automation.dispatcher.service.domain;

import com.home.automation.dispatcher.domain.BoardMessageModel;

import java.util.Collection;

public interface BoardMessageService {

    BoardMessageModel saveBoardMessageModel(final BoardMessageModel boardMessage);

    BoardMessageModel getBoardMessageByMessageId(final String messageId);

    Collection<BoardMessageModel> findLastBoardMessages(int lastCount);
}
