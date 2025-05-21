package com.example.app.webSocket.handlers;


import com.example.app.webSocket.WebSocketCommandHandler;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class GetThemeHandler implements WebSocketCommandHandler {

    @Override
    public String getType() {
        return "get_theme";
    }

    @Override
    public void handle(WebSocketSession session, JsonNode data) throws Exception {
        String theme = "生成されたお題";
        session.sendMessage(new TextMessage("{\"type\": \"theme\", \"data\": \"" + theme + "\"}"));
    }
}
