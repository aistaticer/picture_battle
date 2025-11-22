package com.example.app.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;
import com.example.app.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("""
        SELECT u.team.name 
        FROM User u 
        WHERE u.id = :userId
    """)
    String findTeamNameByUserId(@Param("userId") UUID userId);
}
