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
    public void handle(WebSocketSession session, JsonNode actionType, JsonNode payload) throws Exception {
        String actionTypeStr = actionType.asText();

        switch (actionTypeStr) {
            //case "save" -> handleSave(session, payload);
            //case "get" -> handleGet(session, payload);
            case "updateTile" -> handleupdateTile(session, payload);
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
        String roomId = payload.get("roomId").asText();
        String senderId = payload.get("senderId").asText();
        String boardId = payload.get("boardId").asText();

        System.err.println(redisService.getBoard(boardId));

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
        //broadcaster.broadcastToRoom(roomId,senderId,boardMap);
    }

    /**
     * redisに送られてきたデータを保存する。
     * @param session WebSocketセッション
     * @param payload リクエスト本体（boardId,boardの中身）
     */
    /*private void handleSave(WebSocketSession session, JsonNode payload) throws Exception {

        System.err.println("save");
        // payload -> BoardPayloadDto へ変換
        BoardPayloadDto boardPayload = objectMapper.treeToValue(payload, BoardPayloadDto.class);

        String roomId = boardPayload.getRoomId();

        BoardDTO boardDTO = boardPayload.getBoard();

        String senderId = boardPayload.getSenderId();
        System.out.println("senderId = " + senderId);

        broadcaster.broadcastToRoom(roomId,senderId,boardDTO);

        // redisに保存
        Board board = BoardMapper.toBoard(boardPayload.getBoard());
        redisService.save(board.getBoardId(),board);
    }*/

    /**
     * 該当のboardIdのboardの状態を送る
     * @param session WebSocketセッション
     * @param payload リクエスト本体（boardId,boardの中身）
     */
    /*private void handleGet(WebSocketSession session, JsonNode payload) throws Exception {
        String boardId = payload.get("boardId").asText();
        String roomId = payload.get("roomId").asText();

        // nullでも例外を出さないためにoptionalに格納
        Optional<BoardDTO> optionalBoard = redisService.get(boardId);

        System.err.println(redisService.get(boardId));

        if (optionalBoard.isPresent()) {
            BoardDTO boardDTO = optionalBoard.get();

            // DTOを作成
            BoardResponseDTO responseDTO = new BoardResponseDTO();
            responseDTO.setType("board");
            responseDTO.setAction("game");
            responseDTO.setBoard(boardDTO);

            String responseDTOJson = objectMapper.writeValueAsString(responseDTO);

            session.sendMessage(new TextMessage(responseDTOJson));
        }
    }*/

}
