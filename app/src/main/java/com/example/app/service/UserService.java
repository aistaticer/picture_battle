package com.example.app.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.app.entity.UserEntity;

import com.example.app.repository.UserRepository;

@Service
public class UserService {
	  private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

		public UserEntity registerUser(UUID room_id, String name){
        UUID userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        UserEntity user = new UserEntity();
				user.setId(userId);
        user.setRoom_id(room_id);
        user.setName(name);
        return userRepository.save(user);
    }
}
