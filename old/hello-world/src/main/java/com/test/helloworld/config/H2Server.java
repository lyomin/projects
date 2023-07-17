package com.test.helloworld.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class H2Server {
    private org.h2.tools.Server webServer;

    private org.h2.tools.Server tcpServer;

    @EventListener(ApplicationReadyEvent.class)
    public void start() throws java.sql.SQLException {
        this.webServer = org.h2.tools.Server.createWebServer("-webPort", "8085", "-tcpAllowOthers").start();
        this.tcpServer = org.h2.tools.Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        this.tcpServer.stop();
        this.webServer.stop();
    }
}
