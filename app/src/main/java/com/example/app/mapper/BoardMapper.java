package com.example.app.mapper;

import com.example.app.model.Board;
import com.example.app.model.Tile;

import java.util.stream.Collectors;
import java.util.List;

import com.example.app.dto.BoardDTO;
import com.example.app.dto.TileDTO;

public class BoardMapper {
    public static Board toBoard(BoardDTO dto) {
        Board board = new Board();
        board.setBoardId(dto.getBoardId());

				// tiles(List<List<TileDTO>>) を List<List<Tile>> に変換
        List<List<Tile>> tileList = dto.getTiles().stream()
            .map(row -> row.stream()
                .map(TileMapper::toTile)
                .collect(Collectors.toList()))
            .collect(Collectors.toList());

        board.setTiles(tileList);
        return board;
    }

    public static BoardDTO toBoardDTO(Board board) {
        BoardDTO dto = new BoardDTO();
        dto.setBoardId(board.getBoardId());

				// tiles(List<List<TileDTO>>) を List<List<Tile>> に変換
        List<List<TileDTO>> tileDtoList = board.getTiles().stream()
            .map(row -> row.stream()
                .map(TileMapper::toTileDTO)
                .collect(Collectors.toList()))
            .collect(Collectors.toList());
						
        dto.setTiles(tileDtoList);
        return dto;
    }
}