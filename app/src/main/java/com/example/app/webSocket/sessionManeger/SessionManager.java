package com.example.app.webSocket.sessionManeger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocketセッションを管理するクラス
 */
@Component
public class SessionManager {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();
    private final Map<String, String> sessionIdToRoomId = new ConcurrentHashMap<>();

    public void addSession(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }


    public void assignToRoom(String roomId, WebSocketSession session) {
        roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet())
                    .add(session);
        sessionIdToRoomId.put(session.getId(), roomId);
    }

    public void removeFromRoom(String roomId, WebSocketSession session) {
        Set<WebSocketSession> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            sessions.remove(session);
            // ルームが空になった場合はルーム自体を削除
            if (sessions.isEmpty()) {
                roomSessions.remove(roomId);
            }
        }
        sessionIdToRoomId.remove(session.getId());
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session.getId());
        // セッションが削除される際は、ルームからも削除
        String roomId = sessionIdToRoomId.remove(session.getId());
        if (roomId != null) {
            removeFromRoom(roomId, session);
        }
    }

    public Optional<WebSocketSession> getSession(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public Collection<WebSocketSession> getAllSessions() {
        return sessions.values();
    }

    public Optional<Set<WebSocketSession>> getSessionsByRoomId(String roomId) {
        Set<WebSocketSession> sessions = roomSessions.get(roomId);
        if (sessions == null) {
            return Optional.empty();
        }
        return Optional.of(Collections.unmodifiableSet(sessions)); // 変更不可セットで返す場合
    }
    

    public Optional<String> getRoomIdBySessionId(String sessionId) {
        return Optional.ofNullable(sessionIdToRoomId.get(sessionId));
    }

    public Optional<String> getRoomIdBySession(WebSocketSession session) {
        return getRoomIdBySessionId(session.getId());
    }
}
