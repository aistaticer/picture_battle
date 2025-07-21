package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.app.dto.BoardDTO;
import com.example.app.model.Board;
import com.example.app.model.Tile;
import com.example.app.mapper.BoardMapper;

import java.util.List;
import java.util.Optional;
import java.time.Duration;

@Service
public class RedisService {
	/*private final RedisJsonRepository repository;

	public RedisService(RedisJsonRepository repository){
		this.repository = repository;
	}*/

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	public void save(String boarId, Board board) {
    redisTemplate.opsForValue().set(boarId, board, Duration.ofMinutes(60));
	}

	public Optional<BoardDTO> get(String boardId) {
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
	}
}
