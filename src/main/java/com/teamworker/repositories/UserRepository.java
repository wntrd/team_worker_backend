package com.teamworker.repositories;

import com.teamworker.models.Position;
import com.teamworker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> getUsersByPosition(Position position);
}
