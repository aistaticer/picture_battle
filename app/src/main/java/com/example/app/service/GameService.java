package com.example.app.service;

import com.example.app.repository.jpa.GameRepository;
import com.example.app.repository.jpa.UserRepository;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.app.entity.Game;
import com.example.app.entity.User;

@Service
public class GameService {

		private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

		public Game getGameById(String id){
        return gameRepository.findById(UUID.fromString(id)).orElse(null);
    }
}
