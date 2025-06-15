package com.example.app.webSocket;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.example.app.dto.BoardDTO;
import com.example.app.dto.TileDTO;
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
    // クライアントから受信した WebSocket メッセージ（JSON形式）を Jackson の JsonNode にパースする
    JsonNode root = mapper.readTree(message.getPayload());

    // メッセージ内の "type" フィールドを取得して、どの処理をすべきか判断する
    String type = root.get("type").asText();

    // メッセージ内の "data" フィールドを取得（処理対象のデータ）
    JsonNode actionType = root.get("action");

    JsonNode payload = root.get("payload");

    // type に応じたコマンドハンドラに処理を委譲する
    dispatcher.dispatch(type, session, actionType, payload);
}
}
