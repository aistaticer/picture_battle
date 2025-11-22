package com.example.app.service;
import java.util.*;

import com.example.app.model.Board;
import com.example.app.model.Tile;
import com.example.app.entity.Team;

public class BoardGameService {
	public Board getInitialBoard(int size, List<Team> teams) {

		System.err.println("マス目size: " + size);
		int width = size;
		int height = size;
		int y = 0;
		Map<String, Tile> tileMap = new HashMap<>();

		Random random = new Random();

		for (int z = 0; z < height; z++) {
			for (int x = 0; x < width; x++) {
				Tile tile = new Tile();
				tile.setPosition(Arrays.asList(x, y, z));
				tile.setType("empty"); // 必要に応じて初期タイプなど設定

				String key = x + "-" + y + "-" + z;
				tileMap.put(key, tile);

			}

		}

		// 各チームの盤面に最初のタイルをランダムに設定する
		teams.forEach(team -> {
			// ランダムなキーを選んで clickable にする
			List<String> keys = new ArrayList<>(tileMap.keySet());
			String clickableKey = keys.get(random.nextInt(keys.size()));

			Tile t = tileMap.get(clickableKey);

			t.setType("clickable" + team.getName());
			tileMap.put(clickableKey, t);
		});


		return new Board("board1",tileMap);
	}
}
