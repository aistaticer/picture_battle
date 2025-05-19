package com.example.app.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.app.dto.Board;
import com.example.app.dto.Tile;

public class GameService {
	public Board getInitialBoard(int size) {
		List<List<Tile>> tiles = new ArrayList<>();

		int width = size;
		int height = size;
		int y = 0;

		for (int z = 0; z < height; z++) {
			List<Tile> row = new ArrayList<>();
			for (int x = 0; x < width; x++) {
				row.add(new Tile("empty", Arrays.asList(x, y, z)));
			}
			tiles.add(row);
		}

		return new Board(tiles);
	}
}
