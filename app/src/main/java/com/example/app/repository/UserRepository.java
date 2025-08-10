package com.example.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.app.entity.UserEntity;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    
}
