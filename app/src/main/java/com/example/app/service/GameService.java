package com.example.app.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.app.dto.BoardDTO;
import com.example.app.dto.TileDTO;

public class GameService {
	public BoardDTO getInitialBoard(int size) {
		List<List<TileDTO>> tiles = new ArrayList<>();

		int width = size;
		int height = size;
		int y = 0;

		for (int z = 0; z < height; z++) {
			List<TileDTO> row = new ArrayList<>();
			for (int x = 0; x < width; x++) {
				row.add(new TileDTO("empty", Arrays.asList(x, y, z)));
			}
			tiles.add(row);
		}

		return new BoardDTO(tiles);
	}
}
