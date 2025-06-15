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
public class BoardCommandHandler implements WebSocketCommandHandler {

    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    public BoardCommandHandler(RedisService redisService, ObjectMapper objectMapper) {
        this.redisService = redisService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getType() {
        return "board";
    }

    @Override
    public void handle(WebSocketSession session, JsonNode actionType, JsonNode payload) throws Exception {
        String actionTypeStr = actionType.asText();

        switch (actionTypeStr) {
            case "save" -> handleSave(session, payload);
            case "get" -> handleGet(session);
        }
    }

    private void handleSave(WebSocketSession session, JsonNode payload) throws Exception {
        Board board = objectMapper.treeToValue(payload, Board.class);
        redisService.save("1",board);
    }

    private void handleGet(WebSocketSession session) throws Exception {

        Optional<Board> optionalBoard = redisService.get("1");
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            String boardJson = objectMapper.writeValueAsString(board);
            session.sendMessage(new TextMessage(boardJson));
        }
    }

}


