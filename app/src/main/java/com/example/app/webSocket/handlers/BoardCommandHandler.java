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
import com.example.app.dto.BoardPayloadDto;
import com.example.app.mapper.BoardMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class BoardCommandHandler implements WebSocketCommandHandler {

    private final RedisService redisService;
    private final ObjectMapper objectMapper;
    private final WebSocketBroadcaster broadcaster;

    /**
     * RedisとObjectMapperを使ってBoardの保存や取得を行う。
     */
    public BoardCommandHandler(RedisService redisService, ObjectMapper objectMapper, WebSocketBroadcaster broadcaster) {
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
        return "board";
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
            case "save" -> handleSave(session, payload);
            case "get" -> handleGet(session, payload);
        }
    }

    /**
     * redisに送られてきたデータを保存する。
     * @param session WebSocketセッション
     * @param payload リクエスト本体（boardId,boardの中身）
     */
    private void handleSave(WebSocketSession session, JsonNode payload) throws Exception {

        // payload -> BoardPayloadDto へ変換
        BoardPayloadDto boardPayload = objectMapper.treeToValue(payload, BoardPayloadDto.class);

        String roomId = boardPayload.getRoomId();

        Board board = BoardMapper.toBoard(boardPayload.getBoard());

        System.err.println("save実行"+" roomId:"+roomId + " board:"+board);
        redisService.save(board.getBoardId(),board);
    }

    /**
     * 該当のboardIdのboardの状態を送る
     * @param session WebSocketセッション
     * @param payload リクエスト本体（boardId,boardの中身）
     */
    private void handleGet(WebSocketSession session, JsonNode payload) throws Exception {
        String boardId = payload.get("boardId").asText();

        // nullでも例外を出さないためにoptionalに格納
        Optional<Board> optionalBoard = redisService.get(boardId);

        System.err.println(redisService.get(boardId));

        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            String boardJson = objectMapper.writeValueAsString(board);
            session.sendMessage(new TextMessage(boardJson));
        }
    }

}


