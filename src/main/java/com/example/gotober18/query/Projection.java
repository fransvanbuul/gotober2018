package com.example.gotober18.query;

import com.example.gotober18.command.IssuedEvt;
import com.example.gotober18.command.RedeemedEvt;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("query")
public class Projection {

    private final EntityManager entityManager;

    @EventHandler
    public void on(IssuedEvt evt) {
        entityManager.persist(new Cardsummary(evt.getId(), evt.getAmount(), evt.getAmount()));
    }

    @EventHandler
    public void on(RedeemedEvt evt) {
        entityManager.find(Cardsummary.class, evt.getId()).remainingValue -= evt.getAmount();
    }

    @QueryHandler
    public List<Cardsummary> handle(FindAllQuery query) {
        return entityManager.createQuery("select c from Cardsummary c", Cardsummary.class).getResultList();
    }


}
