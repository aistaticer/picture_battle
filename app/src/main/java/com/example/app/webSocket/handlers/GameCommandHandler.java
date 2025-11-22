package com.example.app.webSocket.handlers;


import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.example.app.service.GameService;
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
import com.example.app.repository.jpa.TeamRepository;
import com.example.app.service.RedisService;
import com.example.app.webSocket.WebSocketBroadcaster;
import com.example.app.context.DispatchContext;
import com.example.app.dto.BoardDTO;
import com.example.app.dto.BoardPayloadDto;
import com.example.app.dto.BoardResponseDTO;
import com.example.app.dto.TileDTO;
import com.example.app.mapper.BoardMapper;
import com.example.app.entity.Game;
import com.example.app.entity.Team;
import com.example.app.entity.User;
import com.example.app.entity.WebSocket;
import com.example.app.webSocket.sessionManeger.SessionManager;
import com.example.app.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class GameCommandHandler implements WebSocketCommandHandler {

    private final RedisService redisService;
    private final ObjectMapper objectMapper;
    private final WebSocketBroadcaster broadcaster;
    private Map<String, TileDTO> tileMap = new HashMap<>();
    private final Map<String, Map<String, TileDTO>> gameBoards = new ConcurrentHashMap<>();
    private final SessionManager sessionManager;
    private final UserService userService;
    private final GameService gameService;
    private final TeamRepository teamRepository;

    /**
     * RedisとObjectMapperを使ってBoardの保存や取得を行う。
     */
    public GameCommandHandler(RedisService redisService, ObjectMapper objectMapper, WebSocketBroadcaster broadcaster, SessionManager sessionManager, UserService userService, GameService gameService, TeamRepository teamRepository) {
        this.redisService = redisService;
        this.objectMapper = objectMapper;
        this.broadcaster = broadcaster;
        this.sessionManager = sessionManager;
        this.userService = userService;
        this.gameService = gameService;
        this.teamRepository = teamRepository;
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
    public void handle(WebSocketSession session, DispatchContext dispatchContext) throws Exception {
        String actionType = dispatchContext.getActionType();

        switch (actionType) {
            case "join" -> handleJoin(session, dispatchContext.getPayload());
        }
    }

    /**
     * 該当のboardIdのboardの状態を送る
     * @param session WebSocketセッション
     * @param payload リクエスト本体（boardId,boardの中身）
     */
    private void handleJoin(WebSocketSession session, JsonNode payload) throws Exception {
        String boardId = payload.get("boardId").asText();
        String gameId = payload.get("gameId").asText();

        broadcaster.printAllSessions();

        System.err.println("payload: " + payload);

        BoardDTO boardDTO = redisService.getBoard(boardId);

        // DTOを作成
        BoardResponseDTO responseDTO = new BoardResponseDTO();
        responseDTO.setType("game");
        responseDTO.setAction("join");
        

        // userIdをUUIDに変換してUserを取得
        User user = userService.getUserById(payload.get("userId").asText());   
        
        // クライアントから渡されたuserIdをUUIDに変換してチーム名を取得
        String myTeamName = userService.getTeamNameByUserId(user.getId());
        // セッションにチーム名を保存。都度DBに接続しなくて済むようにしている
        session.getAttributes().put("myTeamName", myTeamName);
        // 今回はすでに取得したチーム名をそのまま使用する
        responseDTO.setMyTeamName(myTeamName);

        List<String> teamNames = teamRepository.findTeamNamesByGameId(UUID.fromString(gameId));
        responseDTO.setTeamNames(teamNames);

        // tobeのboard
        responseDTO.setBoard(boardDTO);

        String responseDTOJson = objectMapper.writeValueAsString(responseDTO);

        // このやり方はオートスケーティングに対応できないからいつか変える
        gameBoards.put(gameId, tileMap);

        System.err.println(responseDTOJson);

        // セッションをルームに割り当てる
        sessionManager.assignTogame(gameId, session);

        session.sendMessage(new TextMessage(responseDTOJson));
    }

}


