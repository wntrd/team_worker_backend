package com.teamworker.repositories;

import com.teamworker.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {

    Position findPositionById(Long id);
}
