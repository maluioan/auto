package com.home.automation.dispatcher.service.domain.impl;

import com.home.automation.dispatcher.domain.BoardMessageModel;
import com.home.automation.dispatcher.repository.BoardRepository;
import com.home.automation.dispatcher.service.domain.BoardMessageService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class DefaultBoardMessageService implements BoardMessageService {

    @Autowired
    private BoardRepository boardRepository;

    public BoardMessageModel saveBoardMessageModel(final BoardMessageModel boardMessage) {
        return boardRepository.saveBoardMessageModel(boardMessage);
    }

    @Override
    public BoardMessageModel getBoardMessageByMessageId(String messageId) {
        Assert.notNull(messageId, "Message Id should not be null");

        final Optional<BoardMessageModel> messageByMessageId = boardRepository.findMessageByMessageId(messageId);
        return messageByMessageId.orElse(null);
    }

    @Override
    public Collection<BoardMessageModel> findLastBoardMessages(int lastCount) {
        return lastCount > 0 ? boardRepository.findLastMessages(lastCount) : CollectionUtils.emptyCollection();
    }
}
