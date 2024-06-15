package com.teamworker.services.impl;

import com.teamworker.models.Position;
import com.teamworker.models.User;
import com.teamworker.repositories.PositionRepository;
import com.teamworker.services.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    public Position add(Position position) {
        log.info("IN add - {} position added", position.getName());
        return positionRepository.save(position);
    }

    @Override
    public Position update(Long id, Position position) {
        Position foundPosition = positionRepository.findById(id).orElse(null);
        if(foundPosition == null) {
            return null;
        }
        foundPosition.setName(position.getName());
        foundPosition.setProject(position.getProject());
        log.info("IN update - {} position updated", position.getId());
        return positionRepository.save(foundPosition);
    }

    @Override
    public void delete(Long id) {
        Position position = positionRepository.findById(id).orElse(null);
        for (User user : position.getUsers()) {
            user.getPosition().remove(position);
        }

        positionRepository.deleteById(id);
        log.info("IN delete - {} position deleted", id);
    }

    @Override
    public List<Position> getAll() {
        List<Position> positions = positionRepository.findAll();
        if(positions.isEmpty()) {
            return null;
        }
        log.info("IN getAll - {} positions found", positions.size());
        return positions;
    }

    @Override
    public Position getById(Long id) {
        Position position = positionRepository.findById(id).orElse(null);
        if(position == null) {
            return null;
        }
        log.info("IN getById {} position found", position.getName());
        return position;
    }
}
