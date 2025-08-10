package com.example.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocket {
	String id;
	String instance_id;
	String room_id;
	String user_id;
}
