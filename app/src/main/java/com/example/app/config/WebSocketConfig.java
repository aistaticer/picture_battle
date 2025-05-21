package com.example.app.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import com.example.app.webSocket.EchoHandler;
import com.example.app.webSocket.WebSocketDispatcher;

import org.slf4j.Logger;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketDispatcher dispatcher;

    public WebSocketConfig(WebSocketDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new EchoHandler(dispatcher), "/ws")
                .setAllowedOrigins("*");

    }
}

