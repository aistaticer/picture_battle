package com.example.app.webSocket;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.app.context.DispatchContext;

@Component
public class WebSocketDispatcher {

    private final Map<String, WebSocketCommandHandler> handlerMap = new HashMap<>();

    public WebSocketDispatcher(List<WebSocketCommandHandler> handlers) {
			for (WebSocketCommandHandler handler : handlers) {
				handlerMap.put(handler.getType(), handler);
			}
    }

    public void dispatch(DispatchContext dispatchContext) throws Exception {
			WebSocketCommandHandler handler = handlerMap.get(dispatchContext.getType());
			if (handler != null) {	
				handler.handle(dispatchContext.getSession(), dispatchContext);
			} else {
				dispatchContext.getSession().sendMessage(new TextMessage("{\"error\": \"Unknown command\"}"));
			}
    }
}
