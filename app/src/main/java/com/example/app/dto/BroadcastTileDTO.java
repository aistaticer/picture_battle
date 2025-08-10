package com.example.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BroadcastTileDTO {
	private String type;
	private String action;
	private String roomId;
	private String senderId;
	@JsonProperty("updateTile")
	private TileDTO tile;
}
