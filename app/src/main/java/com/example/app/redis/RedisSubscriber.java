package com.example.app.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import com.example.app.dto.BroadcastTileDTO;
import com.example.app.webSocket.WebSocketBroadcaster;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisSubscriber implements MessageListener {

    @Autowired
    private ObjectMapper objectMapper; 

    @Autowired
    private WebSocketBroadcaster broadcaster;
    
    public RedisSubscriber(ObjectMapper objectMapper, WebSocketBroadcaster broadcaster) {
        this.objectMapper = objectMapper;
        this.broadcaster = broadcaster;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());

        System.out.println("Received message: " + body + " from channel: " + channel);

        try {
            // まず中身のJSON文字列を普通のJSONに戻す
            String json = objectMapper.readValue(body, String.class);

            // それをDTOに変換
            BroadcastTileDTO dto = objectMapper.readValue(json, BroadcastTileDTO.class);

            dto.setType("server");
            dto.setAction("send");
            System.out.println("変換結果: " + dto);

            // ここで WebSocket 経由で他のクライアントに送信したりできる
            broadcaster.broadcastToRoom(dto.getRoomId(), dto.getSenderId(), dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
