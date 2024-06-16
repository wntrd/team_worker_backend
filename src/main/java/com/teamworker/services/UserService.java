package com.teamworker.services;

import com.teamworker.models.Position;
import com.teamworker.models.Project;
import com.teamworker.models.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User register(User user);

    List<User> getAll();

    List<User> getAllByManager(Long id);

    Map<User, List<Integer>> getAllWithStatsByManager(Long id);

    List<User> getAllManagers();

    User findByUsername(String username);

    User getById(Long id);

    List<User> findUsersWithPosition(Position position);

    User update(Long id, User user);

    void delete(Long id);

    User getCurrentUser();

    boolean isAdmin(User user);

    User addPosition(Long id, Position position);

    User deletePosition(Long id, Position position);

    User updateRole(Long id, String role);
}
