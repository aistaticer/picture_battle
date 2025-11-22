package com.example.app.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import com.example.app.entity.Game;

public interface GameRepository extends JpaRepository<Game, UUID> {
    
}