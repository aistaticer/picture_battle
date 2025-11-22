package com.example.app.webSocket.handlers;


import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

//import com.example.app.dto.Board;
import com.example.app.service.RedisService;
import com.example.app.webSocket.WebSocketBroadcaster;
import com.example.app.webSocket.WebSocketCommandHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.app.model.Board;
import com.example.app.model.Tile;
import com.example.app.model.ex;
import com.example.app.service.RedisService;
import com.example.app.webSocket.WebSocketBroadcaster;
import com.example.app.dto.BoardPayloadDto;
import com.example.app.dto.BoardResponseDTO;
import com.example.app.dto.TileDTO;
import com.example.app.mapper.BoardMapper;
import com.example.app.mapper.TileMapper;
import com.example.app.context.DispatchContext;
import com.example.app.dto.BoardDTO;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class BoardCommandHandler implements WebSocketCommandHandler {

    private final RedisService redisService;
    private final ObjectMapper objectMapper;
    private final WebSocketBroadcaster broadcaster;
	private final RedisTemplate<String, Object> redisTemplate;

    /**
     * RedisとObjectMapperを使ってBoardの保存や取得を行う。
     */
    public BoardCommandHandler(RedisService redisService, ObjectMapper objectMapper, WebSocketBroadcaster broadcaster, RedisTemplate<String, Object> redisTemplate) {
        this.redisService = redisService;
        this.objectMapper = objectMapper;
        this.broadcaster = broadcaster;
		this.redisTemplate = redisTemplate;
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
    public void handle(WebSocketSession session, DispatchContext dispatchContext) throws Exception {
        String actionType = dispatchContext.getActionType();

        switch (actionType) {
            //case "save" -> handleSave(session, payload);
            //case "get" -> handleGet(session, payload);
            case "updateTile" -> handleupdateTile(session, dispatchContext.getPayload());
        }
    }

    /**
     * redisに送られてきたtileを同一ルームのユーザー等に送り、boardに変更してredisに保存。
     * @param session WebSocketセッション
     * @param payload リクエスト本体（boardId,boardの中身）
     */
    private void handleupdateTile(WebSocketSession session, JsonNode payload) throws Exception {
        System.err.println("updateTile");
        System.err.println("payload: " + payload);
        String gameId = payload.get("gameId").asText();
        String senderId = payload.get("senderId").asText();
        String boardId = payload.get("boardId").asText();

        JsonNode updateTilesNode = payload.get("updateTiles");
        
        // updateTilesNode → tiles 部分をDTOに変換
        Map<String, TileDTO> tiles = objectMapper.convertValue(
            updateTilesNode,
            new TypeReference<Map<String, TileDTO>>() {}
        );

        BoardDTO updateTilesDTO = new BoardDTO(boardId, tiles);

        try {
            //redisService.updateTile(boardId, tileDTO);
            System.err.println("updateTilesDTO: " + updateTilesDTO);
            redisService.updateTile(boardId, updateTilesDTO);
            redisTemplate.convertAndSend("board-updates", payload.toString());  
        } catch (Exception e) {
            e.printStackTrace();
        }
        //broadcaster.broadcastTogame(gameId,senderId,boardMap);
    }
}
