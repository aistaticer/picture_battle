package com.example.app.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.app.entity.User;
import com.example.app.repository.jpa.UserRepository;

@Service
public class UserService {
	  private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUserById(String id){
        return userRepository.findById(UUID.fromString(id)).orElse(null);
    }

    public String getTeamNameByUserId(UUID userId) {
        return userRepository.findTeamNameByUserId(userId);
    }
}
