package com.example.app.repository.jpa;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.app.entity.Team;

public interface TeamRepository extends JpaRepository<Team, UUID> {
	List<Team> findByGameId(UUID gameId);

	@Query("""
    SELECT t.name
    FROM Team t
    WHERE t.game.id = :gameId
  """)
	List<String> findTeamNamesByGameId(@Param("gameId") UUID gameId);
}
