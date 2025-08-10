package com.example.app.webSocket.handlers;

import org.springframework.web.socket.WebSocketSession;
import com.example.app.webSocket.WebSocketCommandHandler;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.stereotype.Component;
@Component
public class NotifyCommandHandlar implements WebSocketCommandHandler {
	
	@Override
	public String getType() {
		return "notify";
	}
	
	@Override
	public void handle(WebSocketSession session, JsonNode actionType, JsonNode payload) throws Exception {
		String actionTypeStr = actionType.asText();	
		
		switch (actionTypeStr) {
			case "join" -> handleTileNotify(session, payload);
		}
	}
	
	private void handleTileNotify(WebSocketSession session, JsonNode payload) throws Exception {
		String roomId = payload.get("roomId").asText();
		String senderId = payload.get("senderId").asText();
		String boardId = payload.get("boardId").asText();
		
		
	}
}
