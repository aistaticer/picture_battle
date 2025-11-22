package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.app.dto.BoardDTO;
import com.example.app.dto.TileDTO;
import com.example.app.dto.TileResponseDTO;
import com.example.app.model.Board;
import com.example.app.model.Tile;
import com.example.app.mapper.BoardMapper;
import com.example.app.mapper.TileMapper;
import java.util.stream.Collectors;

import java.util.*;
import java.time.Duration;

@Service
public class RedisService {


	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	public RedisService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper){
		this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
	}
	
	public void save(String boardId, Board board) {
    redisTemplate.opsForValue().set(boardId, board, Duration.ofMinutes(60));
	}

	public void saveBoard(String boardId, Board board){
		// 既存の boardId のデータを削除
		redisTemplate.delete(boardId);
		
		for (Map.Entry<String, Tile> entry : board.getTiles().entrySet()) {
    	redisTemplate.opsForHash().put(boardId, entry.getKey(), entry.getValue());
		}
	}

	/**
	 * boardのtileを更新する
	 * @param boardId
	 * @param updateTilesDTO
	 */
	public void updateTile(String boardId, BoardDTO updateTilesDTO){
		System.err.println("updateTile");

		System.err.println("updateTilessssDTO: " + updateTilesDTO);
		for (Map.Entry<String, TileDTO> entry : updateTilesDTO.getTiles().entrySet()) {
			String key = entry.getKey();
			TileDTO tileDTO = entry.getValue();
			Tile tile = TileMapper.toTile(tileDTO);
			redisTemplate.opsForHash().put(boardId, key, tile);
		}
		
		/*Tile tile = TileMapper.toTile(tileDTO);
		
		// positionからkeyを作成
		List<Integer> position = tileDTO.getPosition();
		String key = position.stream()
				.map(String::valueOf)
				.collect(Collectors.joining("-"));
		
		redisTemplate.opsForHash().put(boardId, key, tile);
		*/
	}

	/**
	 * boardを取得する
	 * @param boardId
	 * @return
	 */
	public BoardDTO getBoard(String boardId) {
			Map<Object, Object> entries = redisTemplate.opsForHash().entries(boardId);

			Map<String, TileDTO> boardMap = new HashMap<>();
			for (Map.Entry<Object, Object> entry : entries.entrySet()) {
					String key = (String) entry.getKey();
					Tile tile = (Tile) entry.getValue();

					// Tile → TileDTO 変換処理
					TileDTO tileDTO = new TileDTO();
					tileDTO.setType(tile.getType());
					tileDTO.setPosition(tile.getPosition());

					boardMap.put(key, tileDTO);
			}

			BoardDTO boardDTO = new BoardDTO();
			boardDTO.setBoardId(boardId);
			boardDTO.setTiles(boardMap);

			return boardDTO;
	}


	/*public Optional<BoardDTO> get(String boardId) {
    Object obj = redisTemplate.opsForValue().get(boardId);

    // Redisに何もない（null）の場合
    if (obj == null) {
        return Optional.empty();
    }

    // キャストしてDTOに変換（objはBoard型である前提）
    if (obj instanceof Board board) {
        BoardDTO boardDTO = BoardMapper.toBoardDTO(board);
        return Optional.of(boardDTO);
    }

    // 想定外の型だった場合（安全のため）
    return Optional.empty();
	}*/
}
