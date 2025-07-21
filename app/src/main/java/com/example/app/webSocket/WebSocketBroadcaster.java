package com.example.app.webSocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.example.app.dto.BoardDTO;
import com.example.app.dto.BoardResponseDTO;
import com.example.app.dto.TileDTO;
import com.example.app.dto.TileResponseDTO;
import com.example.app.dto.TileResponseDTO;
import com.example.app.model.Board;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.Map;

@Component
public class WebSocketBroadcaster {

  // roomId → Set<Session>
	private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

  // sessionId → roomId
  private final Map<String, String> sessionToRoom = new ConcurrentHashMap<>();

	private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

	/** 
	 * ルームIDごとにセッションを登録＠
	*/
	public void registerSession(String roomId, WebSocketSession session) {
		roomSessions
			.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet())
			.add(session);
    
    sessionToRoom.put(session.getId(), roomId);
	}

	// セッションをルームから削除
  public void removeSession(WebSocketSession session) {
    String roomId = sessionToRoom.remove(session.getId());
    if (roomId == null) return;

    Set<WebSocketSession> sessions = roomSessions.get(roomId);
    if (sessions != null) {
      sessions.remove(session);
      if (sessions.isEmpty()) {
        roomSessions.remove(roomId); // 空ならクリーンアップ
      }
    }
  }

	// ルーム内のすべてのクライアントに送信
	public void broadcastToRoom(String roomId, String senderId, Object data) {
    System.err.println("broadcastToRoom");
    System.err.println(data);

		Set<WebSocketSession> sessions = roomSessions.get(roomId);
		if (sessions != null) {
			for (WebSocketSession session : sessions) {
				try {
          String userId = (String) session.getAttributes().get("userId");
          if(!userId.equals(senderId)){

            TileResponseDTO tileResponseDTO = new TileResponseDTO();
            tileResponseDTO.setType("server");
            tileResponseDTO.setAction("send");
            tileResponseDTO.setTileDTO((TileDTO)data);

					  session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(tileResponseDTO)));
          }
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
