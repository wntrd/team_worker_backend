package com.teamworker.services;

import com.teamworker.models.Position;
import com.teamworker.models.Project;
import com.teamworker.models.User;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User getById(Long id);

    List<User> findUsersWithPosition(Position position);

    User update(Long id, User user);

    void delete(Long id);

    User getCurrentUser();

    boolean isAdmin(User user);

    boolean isAdminOfProject(User user, Project project);

    User addPosition(Long id, Position position);

    User deletePosition(Long id, Position position);
}
