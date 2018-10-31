package com.example.gotober18.command;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@Slf4j
@Profile("command")
public class Giftcard {

    @AggregateIdentifier
    private UUID id;
    private int remainingValue;

    public Giftcard() {
        log.info("empty ctor");
    }

    @CommandHandler
    public Giftcard(IssueCmd cmd) {
        log.info("processing: {}", cmd);
        if(cmd.getAmount() <= 0) throw new IllegalArgumentException("amount <= 0");
        apply(new IssuedEvt(cmd.getId(), cmd.getAmount()));
    }

    @CommandHandler
    public void handle(RedeemCmd cmd) {
        log.info("processing: {}", cmd);
        if(cmd.getAmount() <= 0) throw new IllegalArgumentException("amount <= 0");
        if(cmd.getAmount() > remainingValue) throw new IllegalArgumentException("amount > remainingValue");
        apply(new RedeemedEvt(cmd.getId(), cmd.getAmount()));
    }

    @EventSourcingHandler
    public void on(IssuedEvt evt) {
        log.info("processing: {}", evt);
        id = evt.getId();
        remainingValue = evt.getAmount();
    }

    @EventSourcingHandler
    public void on(RedeemedEvt evt) {
        log.info("processing: {}", evt);
        remainingValue -= evt.getAmount();
    }

}
