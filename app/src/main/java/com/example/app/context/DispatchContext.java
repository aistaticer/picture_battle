package com.example.app.context;

import com.fasterxml.jackson.databind.JsonNode;
import com.example.app.entity.Game;
import com.example.app.entity.User;
import org.springframework.web.socket.WebSocketSession;
import lombok.Data;

@Data
public class DispatchContext {
	private WebSocketSession session;
    private String type;
    private String actionType;
    private JsonNode payload;
    private Game game;
    private User user;
}
