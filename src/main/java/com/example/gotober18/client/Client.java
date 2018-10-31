package com.example.gotober18.client;

import com.example.gotober18.command.IssueCmd;
import com.example.gotober18.command.RedeemCmd;
import com.example.gotober18.query.Cardsummary;
import com.example.gotober18.query.FindAllQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("client")
public class Client implements CommandLineRunner {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @Override
    public void run(String... args) throws Exception {
        for(int i  = 0; i < 100; i++) {
            UUID id = UUID.randomUUID();
            log.info("sending command");
            commandGateway.sendAndWait(new IssueCmd(id, 100));
            log.info("sending command");
            commandGateway.sendAndWait(new RedeemCmd(id, 30));
            log.info("sending command");
            commandGateway.sendAndWait(new RedeemCmd(id, 30));
        }
        Thread.sleep(100);
        log.info("query result {}", queryGateway.query(new FindAllQuery(), ResponseTypes.multipleInstancesOf(Cardsummary.class)).join());
    }
}
