package com.example.app.webSocket;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WebSocketDispatcher {

    private final Map<String, WebSocketCommandHandler> handlerMap = new HashMap<>();

    public WebSocketDispatcher(List<WebSocketCommandHandler> handlers) {
			for (WebSocketCommandHandler handler : handlers) {
				handlerMap.put(handler.getType(), handler);
			}
    }

    public void dispatch(String type, WebSocketSession session, JsonNode actionType, JsonNode payload) throws Exception {
			WebSocketCommandHandler handler = handlerMap.get(type);
			if (handler != null) {
				handler.handle(session, actionType,payload);
			} else {
				session.sendMessage(new TextMessage("{\"error\": \"Unknown command\"}"));
			}
    }
}
