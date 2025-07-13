package com.example.app.webSocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.Map;

@Component
public class WebSocketBroadcaster {

	private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

	private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

	/** 
	 * セッションを登録
	 * 仮のメソッド。いつか消す
	*/
	public void registerSession(WebSocketSession session) {
		sessions.add(session);
	}

	/** 
	 * ルームIDごとにセッションを登録
	*/
	public void registerSession(String roomId, WebSocketSession session) {
		roomSessions
			.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet())
			.add(session);
	}

	public void removeSession(WebSocketSession session) {
		if (session != null) {
			sessions.remove(session); // ✅ 正しい
		}
	}

	// セッションをルームから削除
	public void removeSession(String roomId, WebSocketSession session) {
		Set<WebSocketSession> sessions = roomSessions.get(roomId);
		if (sessions != null) {
			sessions.remove(session);
			if (sessions.isEmpty()) {
				roomSessions.remove(roomId); // 空ならクリーンアップ
			}
		}
	}

	// ルーム内のすべてのクライアントに送信
	public void broadcastToRoom(String roomId, Object data) {
		Set<WebSocketSession> sessions = roomSessions.get(roomId);
		if (sessions != null) {
			for (WebSocketSession session : sessions) {
				try {
					session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(data)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Set<WebSocketSession> getSessions() {
    return sessions;
  }

	/** 
	 * 登録されたルームIDとセッションを全表示
	 * 仮のメソッド。いつか消す
	**/
	public void printAllSessions() {
		for (Map.Entry<String, Set<WebSocketSession>> entry : roomSessions.entrySet()) {
			String roomId = entry.getKey();
			Set<WebSocketSession> sessions = entry.getValue();

			System.out.println("ルームID: " + roomId);
			for (WebSocketSession session : sessions) {
				System.out.println("  セッションID: " + session.getId());
			}
		}
	}
}
