package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.app.model.Board;
import com.example.app.model.Tile;

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
	public void save(String gameId, Board board) {
    redisTemplate.opsForValue().set(gameId, board, Duration.ofMinutes(60));
	}

	public Optional<Board> get(String gameId) {
		Object obj = redisTemplate.opsForValue().get(gameId);
		if (obj instanceof Board board) {
			return Optional.of(board);
		} else {
			return Optional.empty();
		}
	}
}
