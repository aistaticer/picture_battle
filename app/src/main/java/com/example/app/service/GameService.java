package com.example.app.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.*;

import com.example.app.model.Board;
import com.example.app.model.Tile;
import com.example.app.model.Tile;

public class GameService {
	public Board getInitialBoard(int size) {

		int width = size;
		int height = size;
		int y = 0;
		Map<String, Tile> tileMap = new HashMap<>();

		for (int z = 0; z < height; z++) {
			for (int x = 0; x < width; x++) {
				Tile tile = new Tile();
				tile.setPosition(Arrays.asList(x, y, z));
				tile.setType("empty"); // 必要に応じて初期タイプなど設定

				String key = x + "-" + y + "-" + z;
				tileMap.put(key, tile);
			}

		}

		return new Board("1",tileMap);
	}
}
