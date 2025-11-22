package com.example.app.webSocket.handlers;

import org.springframework.web.socket.WebSocketSession;

import com.example.app.context.DispatchContext;
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
	public void handle(WebSocketSession session, DispatchContext dispatchContext) throws Exception {
		String actionType = dispatchContext.getActionType();	
		
		switch (actionType) {
			case "join" -> handleTileNotify(session, dispatchContext.getPayload());
		}
	}
	
	private void handleTileNotify(WebSocketSession session, JsonNode payload) throws Exception {
		String gameId = payload.get("gameId").asText();
		String senderId = payload.get("senderId").asText();
		String boardId = payload.get("boardId").asText();
		
		
	}
}
