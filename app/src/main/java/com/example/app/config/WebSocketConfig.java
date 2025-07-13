package com.example.app.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import com.example.app.webSocket.EchoHandler;
import com.example.app.webSocket.WebSocketBroadcaster;
import com.example.app.webSocket.WebSocketDispatcher;
import com.example.app.webSocket.WebSocketBroadcaster;

import org.slf4j.Logger;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketDispatcher dispatcher;
    private final WebSocketBroadcaster broadcaster;

    public WebSocketConfig(WebSocketDispatcher dispatcher, WebSocketBroadcaster broadcaster) {
        this.dispatcher = dispatcher;
        this.broadcaster = broadcaster;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new EchoHandler(dispatcher,broadcaster), "/ws")
                .setAllowedOrigins("*");

    }
}

