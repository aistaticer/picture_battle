package com.example.app.webSocket;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.example.app.dto.Board;
import com.example.app.dto.Tile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EchoHandler extends TextWebSocketHandler {

	private final ObjectMapper mapper = new ObjectMapper();
	private final WebSocketDispatcher dispatcher;

	public EchoHandler(WebSocketDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		JsonNode root = mapper.readTree(message.getPayload());
		String type = root.get("type").asText();
		JsonNode data = root.get("data");

		dispatcher.dispatch(type, session, data);


	}
}
