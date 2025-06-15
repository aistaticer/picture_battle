package com.example.app.webSocket;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.socket.WebSocketSession;

public interface WebSocketCommandHandler {
    String getType();  // どのtypeを処理するか
    void handle(WebSocketSession session, JsonNode actionType, JsonNode payload) throws Exception; // 処理本体
}
