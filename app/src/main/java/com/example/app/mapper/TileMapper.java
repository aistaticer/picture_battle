package com.example.app.mapper;

import com.example.app.model.Tile;
import com.example.app.dto.TileDTO;

public class TileMapper {
    public static Tile toTile(TileDTO dto) {
        Tile tile = new Tile();
        tile.setType(dto.getType());
        tile.setPosition(dto.getPosition());
        return tile;
    }

    public static TileDTO toTileDTO(Tile tile) {
        TileDTO dto = new TileDTO();
        dto.setType(tile.getType());
        dto.setPosition(tile.getPosition());
        return dto;
    }
}
