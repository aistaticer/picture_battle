package com.example.app.webSocket.handlers;


import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

//import com.example.app.dto.Board;
import com.example.app.service.RedisService;
import com.example.app.webSocket.WebSocketBroadcaster;
import com.example.app.webSocket.WebSocketCommandHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.app.model.Board;
import com.example.app.model.Tile;
import com.example.app.model.ex;
import com.example.app.service.RedisService;
import com.example.app.webSocket.WebSocketBroadcaster;
import com.example.app.dto.BoardDTO;
import com.example.app.dto.BoardPayloadDto;
import com.example.app.dto.BoardResponseDTO;
import com.example.app.mapper.BoardMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class GameCommandHandler implements WebSocketCommandHandler {

    private final RedisService redisService;
    private final ObjectMapper objectMapper;
    private final WebSocketBroadcaster broadcaster;

    /**
     * RedisとObjectMapperを使ってBoardの保存や取得を行う。
     */
    public GameCommandHandler(RedisService redisService, ObjectMapper objectMapper, WebSocketBroadcaster broadcaster) {
        this.redisService = redisService;
        this.objectMapper = objectMapper;
        this.broadcaster = broadcaster;
    }

    /**
     * このハンドラーが処理するコマンドの種別。
     * @return "board" を返す。
     */
    @Override
    public String getType() {
        return "game";
    }

    /**
     * WebSocketからのリクエストに応じて処理を振り分ける。
     * @param session WebSocketセッション
     * @param actionType 処理タイプ (例: "save", "get")
     * @param payload リクエスト本体
     */
    @Override
    public void handle(WebSocketSession session, JsonNode actionType, JsonNode payload) throws Exception {
        String actionTypeStr = actionType.asText();

        switch (actionTypeStr) {
            case "start" -> handleStart(session, payload);
        }
    }

    /**
     * 該当のboardIdのboardの状態を送る
     * @param session WebSocketセッション
     * @param payload リクエスト本体（boardId,boardの中身）
     */
    private void handleStart(WebSocketSession session, JsonNode payload) throws Exception {
        String boardId = payload.get("boardId").asText();
        String roomId = payload.get("roomId").asText();

        broadcaster.registerSession(roomId,session);
        broadcaster.printAllSessions();

        // nullでも例外を出さないためにoptionalに格納
        Optional<BoardDTO> optionalBoard = redisService.get(boardId);

        if (optionalBoard.isPresent()) {

            BoardDTO boardDTO = optionalBoard.get();

            // DTOを作成
            BoardResponseDTO responseDTO = new BoardResponseDTO();
            responseDTO.setType("game");
            responseDTO.setAction("start");
            responseDTO.setBoard(boardDTO);

            String responseDTOJson = objectMapper.writeValueAsString(responseDTO);

            session.sendMessage(new TextMessage(responseDTOJson));
        }
    }

}


