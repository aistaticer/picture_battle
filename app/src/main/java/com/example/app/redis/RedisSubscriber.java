package com.example.app.redis;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import com.example.app.dto.BroadcastTileDTO;
import com.example.app.dto.TileDTO;
import com.example.app.webSocket.WebSocketBroadcaster;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


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
            // まず一回パースして中の文字列を取り出す
            String innerJson = objectMapper.readValue(body, String.class);

            // それをもう一度JsonNodeに変換
            JsonNode root = objectMapper.readTree(innerJson);

            // JSONをJsonNodeとして直接パース
            JsonNode updateTilesNode = root.get("updateTiles");

            // updateTilesNode → tiles 部分をDTOに変換
            Map<String, TileDTO> updateTiles = objectMapper.convertValue(
                updateTilesNode,
                new TypeReference<Map<String, TileDTO>>() {}
            );

            // それをDTOに変換
            BroadcastTileDTO dto = new BroadcastTileDTO();
            dto.setUpdateTiles(updateTiles);

            dto.setGameId(root.get("gameId").asText());
            dto.setSenderId(root.get("senderId").asText());

            dto.setType("server");
            dto.setAction("send");
            System.out.println("変換結果: " + dto);

            // ここで WebSocket 経由で他のクライアントに送信したりできる
            broadcaster.broadcastTogame(dto.getGameId(), dto.getSenderId(), dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
