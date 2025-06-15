package com.example.app.webSocket.handlers;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

//import com.example.app.dto.Board;
import com.example.app.service.RedisService;
import com.example.app.webSocket.WebSocketCommandHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.app.model.Board;
import com.example.app.model.Tile;
import com.example.app.model.ex;
import com.example.app.service.RedisService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class GetBoardHandler implements WebSocketCommandHandler{

		private final RedisService redisService;
		private final ObjectMapper objectMapper;

		public GetBoardHandler(RedisService redisService, ObjectMapper objectMapper) {
			this.redisService = redisService;
			this.objectMapper = objectMapper;
		}

	  @Override
    public String getType() {
        return "get_Board";
    }

    @Override
    public void handle(WebSocketSession session, JsonNode actionType, JsonNode payloaNode) throws Exception {

				ObjectMapper mapper = new ObjectMapper();

				System.err.println("aaa"+actionType);

				List<Tile> row = List.of(new Tile("A", List.of(0,0)));
				List<List<Tile>> tiles = List.of(row);

				System.err.println(tiles);

				Board board1 = new Board(tiles);

				redisService.save("1",board1);

				Optional<Board> optionalBoard = redisService.get("1");

				if (optionalBoard.isPresent()) {
					Board board = optionalBoard.get();
					String boardJson = mapper.writeValueAsString(board);
					session.sendMessage(new TextMessage(boardJson));
				}
    }
}
