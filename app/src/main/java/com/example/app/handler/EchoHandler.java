package com.example.app.handler;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.example.app.dto.Board;
import com.example.app.dto.Tile;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EchoHandler extends TextWebSocketHandler {
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			String payload = message.getPayload();
			ObjectMapper mapper = new ObjectMapper();
	    Board board = mapper.readValue(payload, Board.class);
      session.sendMessage(new TextMessage("サーバー応答: " + payload));

			System.out.println("受信した type: " + board.getTiles().get(0).get(0).getType());


    }
}
