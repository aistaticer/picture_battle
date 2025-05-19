package com.example.app.config;

import com.example.app.handler.EchoHandler;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import org.slf4j.Logger;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
		private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new EchoHandler(), "/ws")
                .setAllowedOrigins("*");
				logger.info("✅ WebSocket ハンドラ登録開始");
				System.out.println("✅ WebSocket ハンドラ登録開始");
    }
}
