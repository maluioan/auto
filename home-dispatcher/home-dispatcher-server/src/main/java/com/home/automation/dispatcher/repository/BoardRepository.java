package com.home.automation.dispatcher.repository;

import com.home.automation.dispatcher.domain.BoardMessageModel;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class BoardRepository {

    @PersistenceContext
    protected EntityManager entityManager;

    public BoardMessageModel saveBoardMessageModel(final BoardMessageModel boardMessage) {
        // TODO: make hybris interceptor work pt a seta data creeari
        boardMessage.setDateCreated(LocalDateTime.now());
        boardMessage.setDateModified(LocalDateTime.now());
        //
        this.entityManager.persist(boardMessage);
        return boardMessage;
    }

    public Optional<BoardMessageModel> findMessageByMessageId(final String messageId) {
        final Query findMessageQuery = this.entityManager.createNamedQuery(BoardMessageModel.FIND_MESSAGE_BY_MESSAGE_ID);
        findMessageQuery.setParameter("messageId", messageId);

        final List<BoardMessageModel> resultList = findMessageQuery.getResultList();
        return (resultList.size() != 0)
                ? Optional.ofNullable(resultList.get(0))
                : Optional.empty();
    }

    public Collection<BoardMessageModel> findLastMessages(int lastCount) {
        final Query findMessagesQuery = this.entityManager.createNamedQuery(BoardMessageModel.FIND_LAST_MESSAGES_BY_COUNT);
        findMessagesQuery.setMaxResults(lastCount);

        return findMessagesQuery.<BoardMessageModel>getResultList();
    }
}
