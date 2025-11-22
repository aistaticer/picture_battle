package com.example.app.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import com.example.app.repository.jpa.UserRepository;
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
    private final UserRepository userRepository;

    public WebSocketConfig(WebSocketDispatcher dispatcher, WebSocketBroadcaster broadcaster, SessionManager sessionManager,UserRepository userRepository) {
        this.dispatcher = dispatcher;
        this.broadcaster = broadcaster;
        this.sessionManager = sessionManager;
        this.userRepository = userRepository;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new EchoHandler(dispatcher,broadcaster,sessionManager,userRepository), "/ws")
            .addInterceptors(new WebsocketHandshakeInterceptor())
            .setAllowedOrigins("*");

    }
}

