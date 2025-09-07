package com.example.app.service;
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

		// ランダムなキーを選んで clickable にする
		List<String> keys = new ArrayList<>(tileMap.keySet());
		String clickableKey = keys.get(random.nextInt(keys.size()));

		Tile t = tileMap.get(clickableKey);

		// 仮のgameGroupId。いつかDBからの取得に切り替える
		String gameGroupId = "testGameGroupId";
		t.setType("clickable" + gameGroupId);
		tileMap.put(clickableKey, t);

		return new Board("1",tileMap);
	}
}
