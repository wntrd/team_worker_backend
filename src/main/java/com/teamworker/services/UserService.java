package com.teamworker.services;

import com.teamworker.models.Position;
import com.teamworker.models.User;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    List<User> findUsersWithPosition(Position position);

    void delete(Long id);
}
