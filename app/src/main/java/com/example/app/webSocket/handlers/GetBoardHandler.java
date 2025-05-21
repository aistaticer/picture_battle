package com.example.app.webSocket.handlers;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.example.app.dto.Board;
import com.example.app.webSocket.WebSocketCommandHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class GetBoardHandler implements WebSocketCommandHandler{
	  @Override
    public String getType() {
        return "get_Board";
    }

    @Override
    public void handle(WebSocketSession session, JsonNode data) throws Exception {

				ObjectMapper mapper = new ObjectMapper();
				Board board = mapper.treeToValue(data, Board.class);
				String json = mapper.writeValueAsString(
  			  Map.of("tiles", board.getTiles())
				);
        session.sendMessage(new TextMessage(json));
    }
}
