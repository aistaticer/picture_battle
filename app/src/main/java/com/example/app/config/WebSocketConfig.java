package com.example.app.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import com.example.app.webSocket.EchoHandler;
import com.example.app.webSocket.WebSocketBroadcaster;
import com.example.app.webSocket.WebSocketDispatcher;
import com.example.app.webSocket.WebsocketHandshakeInterceptor;
import com.example.app.webSocket.sessionManeger.SessionManager;

import org.slf4j.Logger;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketDispatcher dispatcher;
    private final WebSocketBroadcaster broadcaster;
    private final SessionManager sessionManager;

    public WebSocketConfig(WebSocketDispatcher dispatcher, WebSocketBroadcaster broadcaster, SessionManager sessionManager) {
        this.dispatcher = dispatcher;
        this.broadcaster = broadcaster;
        this.sessionManager = sessionManager;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new EchoHandler(dispatcher,broadcaster,sessionManager), "/ws")
            .addInterceptors(new WebsocketHandshakeInterceptor())
            .setAllowedOrigins("*");

    }
}

