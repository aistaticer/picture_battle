package com.example.app.webSocket;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.example.app.dto.BoardDTO;
import com.example.app.dto.TileDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.app.webSocket.WebSocketBroadcaster;
import com.example.app.entity.WebSocket;
import java.util.*;
import com.example.app.webSocket.sessionManeger.SessionManager;

public class EchoHandler extends TextWebSocketHandler {

	private final ObjectMapper mapper = new ObjectMapper();
	private final WebSocketDispatcher dispatcher;
    private final WebSocketBroadcaster broadcaster;
    
    // sessionId -> session
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    private final SessionManager sessionManager;

	public EchoHandler(WebSocketDispatcher dispatcher, WebSocketBroadcaster broadcaster, SessionManager sessionManager){
		this.dispatcher = dispatcher;
        this.broadcaster = broadcaster;
		this.sessionManager = sessionManager;
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
        String roomId = sessionManager.getRoomIdBySession(session).orElse(null);

        if (roomId != null) {
            // セッションをルームから削除する
            sessionManager.removeFromRoom(roomId, session);
        }

        // セッションを管理から削除する
        sessionManager.removeSession(session);
    }

	@Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        try{
            //System.err.println("内容確認: "+message.getPayload());

            // クライアントから受信した WebSocket メッセージ（JSON形式）を Jackson の JsonNode にパースする
            JsonNode root = mapper.readTree(message.getPayload());

            // メッセージ内の "type" フィールドを取得して、どの処理をすべきか判断する
            String type = root.get("type").asText();

            // メッセージ内の "data" フィールドを取得（処理対象のデータ）
            JsonNode actionType = root.get("action");

            JsonNode payload = root.get("payload");

            // type に応じたコマンドハンドラに処理を委譲する
            dispatcher.dispatch(type, session, actionType, payload);
        }catch(Exception e){
            System.err.println("例外発生:内容確認: "+message.getPayload());
            System.err.println("Jacksonエラー: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
