package com.example.app.webSocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.example.app.dto.BroadcastTileDTO;
import com.example.app.model.Board;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.Map;
import com.example.app.webSocket.sessionManeger.SessionManager;

@Component
public class WebSocketBroadcaster {

  // gameId → Set<Session>
	private final Map<String, Set<WebSocketSession>> gameSessions = new ConcurrentHashMap<>();

  // sessionId → gameId
  private final Map<String, String> sessionTogame = new ConcurrentHashMap<>();

	private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

	private final SessionManager sessionManager;

	public WebSocketBroadcaster(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	/** 
	 * ルームIDごとにセッションを登録＠
	*/
	public void registerSession(String gameId, WebSocketSession session) {
		gameSessions
			.computeIfAbsent(gameId, k -> ConcurrentHashMap.newKeySet())
			.add(session);
	
    sessionTogame.put(session.getId(), gameId);
	}

	// セッションをルームから削除
  public void removeSession(WebSocketSession session) {
    String gameId = sessionTogame.remove(session.getId());
    if (gameId == null) return;

    Set<WebSocketSession> sessions = gameSessions.get(gameId);
    if (sessions != null) {
      sessions.remove(session);
      if (sessions.isEmpty()) {
        gameSessions.remove(gameId); // 空ならクリーンアップ
      }
    }
  }

	/**
	 * ルーム内のすべてのクライアントに送信
	 * @param gameId ルームID
	 * @param senderId 送信元のユーザーID
	 * @param data 送信するデータ
	*/
	public void broadcastTogame(String gameId, String senderId, BroadcastTileDTO data) {
		Optional<Set<WebSocketSession>> sessions = sessionManager.getSessionsBygameId(gameId);

		if (sessions.isPresent()) {
			for (WebSocketSession session : sessions.get()) {
        String userId = (String) session.getAttributes().get("userId");
        if(!userId.equals(senderId)){

					System.err.println("data: " + data);
          // 同時実行の場合にスレッドが順番にmessageを送ってくれるようになる　動作を確認していないため後ほど確認
          synchronized (session) {
            try {
              session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(data)));
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
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
		for (Map.Entry<String, Set<WebSocketSession>> entry : gameSessions.entrySet()) {
			String gameId = entry.getKey();
			Set<WebSocketSession> sessions = entry.getValue();

			System.out.println("ルームID: " + gameId);
			for (WebSocketSession session : sessions) {
				System.out.println("  セッションID: " + session.getId());
			}
		}
	}
}
