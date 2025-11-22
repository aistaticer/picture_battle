package com.example.app.webSocket;

import org.springframework.web.socket.WebSocketSession;
import com.example.app.context.DispatchContext;

public interface WebSocketCommandHandler {
    String getType();  // どのtypeを処理するか
    void handle(WebSocketSession session, DispatchContext dispatchContext) throws Exception; // 処理本体
}

