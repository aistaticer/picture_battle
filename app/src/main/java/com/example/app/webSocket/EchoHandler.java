package com.example.app.webSocket;

import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.id.uuid.UuidGenerator;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.example.app.dto.BoardDTO;
import com.example.app.dto.TileDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.app.webSocket.WebSocketBroadcaster;
import com.example.app.entity.WebSocket;
import com.example.app.repository.jpa.UserRepository;

import java.util.*;
import com.example.app.webSocket.sessionManeger.SessionManager;
import com.example.app.context.DispatchContext;
import com.example.app.entity.User;

public class EchoHandler extends TextWebSocketHandler {

	private final ObjectMapper mapper = new ObjectMapper();
	private final WebSocketDispatcher dispatcher;
    private final WebSocketBroadcaster broadcaster;
    private final UserRepository userRepository;
    
    // sessionId -> session
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    private final SessionManager sessionManager;

	public EchoHandler(WebSocketDispatcher dispatcher, WebSocketBroadcaster broadcaster, SessionManager sessionManager, UserRepository userRepository){
		this.dispatcher = dispatcher;
        this.broadcaster = broadcaster;
		this.sessionManager = sessionManager;
		this.userRepository = userRepository;
	}

    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket接続が確立されました: " + session.getId());

        // セッションを管理する
        sessionManager.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("切断されたセッション: " + session.getId());
        broadcaster.removeSession(session);


        // セッションが所属していたルームを取得する
        String gameId = sessionManager.getGameIdBySession(session).orElse(null);

        if (gameId != null) {
            // セッションをルームから削除する
            sessionManager.removeFromgame(gameId, session);
        }

        // セッションを管理から削除する
        sessionManager.removeSession(session);
    }

    /**
     * WebSocket接続が確立された際に呼び出される。
     * @param session WebSocketセッション
     * @param message WebSocketメッセージ
     */
	@Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        try{
            System.err.println("内容確認: "+message.getPayload());

            // クライアントから受信した WebSocket メッセージ（JSON形式）を Jackson の JsonNode にパースする
            JsonNode root = mapper.readTree(message.getPayload());

            // メッセージ内の "type" フィールドを取得して、どの処理をすべきか判断する
            String type = root.get("type").asText();

            // メッセージ内の "data" フィールドを取得（処理対象のデータ）
            String actionType = root.get("action").asText();

            JsonNode payload = root.get("payload");

            DispatchContext dispatchContext = new DispatchContext();
            dispatchContext.setSession(session);
            dispatchContext.setType(type);
            dispatchContext.setActionType(actionType);
            dispatchContext.setPayload(payload);

            User user = userRepository.findById(UUID.fromString("88bfbd90-9065-49ef-af81-68db708a4043"))
                          .orElse(null);
            dispatchContext.setUser(user);
        
            // type に応じたコマンドハンドラに処理を委譲する
            dispatcher.dispatch(dispatchContext);

        }catch(Exception e){
            System.err.println("例外発生:内容確認: "+message.getPayload());
            System.err.println("Jacksonエラー: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
