package com.example.app.model;

import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import com.example.app.dto.TileDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("board") 
public class Board {
	private String boardId;
	//private List<List<Tile>> tiles;
	Map<String,Tile> tiles;

}
