package com.example.app.controller;

import com.example.app.service.RedisService;
import com.example.app.model.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/board") 
public class RedisController {

	private final RedisService redisService;

	@Autowired
	public RedisController(RedisService redisService) {
			this.redisService = redisService;
	}

	@PostMapping("/save")
	public String saveBoard(@RequestParam String userId, @RequestBody String json) {
			System.out.println("save起動");
			//redisService.save(userId, json);
			return "Saved!";
	}

	@GetMapping("/get")
	public Board getBoard(@RequestParam String userId) {
			System.out.println("get起動");
			Optional<Board> result = redisService.get(userId);
			return result.orElse(null); // なければ null を返す（本番なら 404 エラーハンドリングが望ましい）
	}
}
