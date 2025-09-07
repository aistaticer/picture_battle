package com.example.app.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BroadcastTileDTO {
	private String type;
	private String action;
	private String roomId;
	private String senderId;
	private Map<String,TileDTO> updateTiles;
}
