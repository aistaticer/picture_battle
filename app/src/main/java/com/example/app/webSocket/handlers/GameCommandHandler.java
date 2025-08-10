package com.example.app.webSocket.handlers;


import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

//import com.example.app.dto.Board;
import com.example.app.service.RedisService;
import com.example.app.webSocket.WebSocketBroadcaster;
import com.example.app.webSocket.WebSocketCommandHandler;
import com.example.app.webSocket.sessionManeger.SessionManager;
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
import com.example.app.dto.TileDTO;
import com.example.app.mapper.BoardMapper;
import com.example.app.entity.WebSocket;
import com.example.app.webSocket.sessionManeger.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class GameCommandHandler implements WebSocketCommandHandler {

    private final RedisService redisService;
    private final ObjectMapper objectMapper;
    private final WebSocketBroadcaster broadcaster;
    private Map<String, TileDTO> tileMap = new HashMap<>();
    private final Map<String, Map<String, TileDTO>> roomBoards = new ConcurrentHashMap<>();
    private final SessionManager sessionManager;

    /**
     * RedisとObjectMapperを使ってBoardの保存や取得を行う。
     */
    public GameCommandHandler(RedisService redisService, ObjectMapper objectMapper, WebSocketBroadcaster broadcaster, SessionManager sessionManager) {
        this.redisService = redisService;
        this.objectMapper = objectMapper;
        this.broadcaster = broadcaster;
        this.sessionManager = sessionManager;
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
            case "join" -> handleJoin(session, payload);
        }
    }

    /**
     * 該当のboardIdのboardの状態を送る
     * @param session WebSocketセッション
     * @param payload リクエスト本体（boardId,boardの中身）
     */
    private void handleJoin(WebSocketSession session, JsonNode payload) throws Exception {
        String boardId = payload.get("boardId").asText();
        String roomId = payload.get("roomId").asText();

        broadcaster.printAllSessions();

        System.err.println("payload: " + payload);

        BoardDTO boardDTO = redisService.getBoard(boardId);

        // DTOを作成
        BoardResponseDTO responseDTO = new BoardResponseDTO();
        responseDTO.setType("game");
        responseDTO.setAction("join");

        // tobeのboard
        responseDTO.setBoard(boardDTO);

        String responseDTOJson = objectMapper.writeValueAsString(responseDTO);

        // このやり方はオートスケーティングに対応できないからいつか変える
        roomBoards.put(roomId, tileMap);

        System.err.println(responseDTOJson);

        // セッションをルームに割り当てる
        sessionManager.assignToRoom(roomId, session);

        session.sendMessage(new TextMessage(responseDTOJson));
    }

}


