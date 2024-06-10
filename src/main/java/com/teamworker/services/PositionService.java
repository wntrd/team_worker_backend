package com.teamworker.services;

import com.teamworker.models.Position;

import java.util.List;

public interface PositionService {

    Position add(Position position);

    Position update(Long id, Position position);

    void delete(Long id);

    List<Position> getAll();

    Position getById(Long id);
}
