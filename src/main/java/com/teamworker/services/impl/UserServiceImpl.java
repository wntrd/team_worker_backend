package com.teamworker.services.impl;

import com.teamworker.models.Position;
import com.teamworker.models.Project;
import com.teamworker.models.Role;
import com.teamworker.models.User;
import com.teamworker.models.enums.Status;
import com.teamworker.models.enums.TaskStage;
import com.teamworker.repositories.RoleRepository;
import com.teamworker.repositories.UserRepository;
import com.teamworker.services.TaskService;
import com.teamworker.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TaskService taskService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {

        if (findByUsername(user.getUsername()) != null) {
            return null;
        }

        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);

        User registeredUser = userRepository.save(user);
        log.info("IN register - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        List<User> usersWithoutAdmins = users.stream().filter(user -> !(user.getRoles().contains(roleAdmin))).collect(Collectors.toList());
        log.info("IN getAll - {} users found", usersWithoutAdmins.size());
        return usersWithoutAdmins;
    }

    @Override
    public List<User> getAllManagers() {
        List<User> users = userRepository.findAll();
        Role roleManager = roleRepository.findByName("ROLE_MANAGER");
        List<User> managers = users.stream().filter(user -> (user.getRoles().contains(roleManager))).collect(Collectors.toList());
        log.info("IN getAllManagers - {} users found", managers.size());
        return managers;
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        log.info("IN findByLogin - user found by username: {}", username);
        return user;
    }

    @Override
    public User getById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }

        log.info("IN findById - user found by id: {}", id);
        return user;
    }

    @Override
    public List<User> getAllByManager(Long id) {
        User manager = this.getById(id);
        List<User> users = new ArrayList<>();

        manager.getManagerProjects().stream().forEach(
                project -> project.getPositions().stream().forEach(
                        position -> users.addAll(position.getUsers())
                )
        );

        List<User> usersWithoutDuplicates = new ArrayList<>(new HashSet<>(users));
        return usersWithoutDuplicates;
    }

    @Override
    public Map<User, List<Integer>> getAllWithStatsByManager(Long id) {
        List<User> users = this.getAllByManager(id);

        if(users.isEmpty()) {
            log.warn("IN getAllWithStatsByManager - no users found");
            return null;
        }

        Map<User, List<Integer>> usersWithStats = new LinkedHashMap<>();

        for (User user : users) {
            List<Integer> stats = new ArrayList<>();
            stats.add(taskService.getPercentageOfCompletedOnTime(user.getId()));
            stats.add(taskService.getNumberByAssigneeAndStage(user.getId(), TaskStage.RELEASED.name()));
            usersWithStats.put(user, stats);
        }

        return usersWithStats;
    }

    @Override
    public List<User> findUsersWithPosition(Position position) {
        List<User> users = userRepository.getUsersByPosition(position);

        if(users.isEmpty()) {
            log.warn("IN findWithPosition - no users found with position: {}", position.getId());
            return null;
        }

        log.info("IN findWithPosition - users found");
        return users;
    }

    @Override
    public User update(Long id, User user) {
        User foundUser = this.getById(id);
        if(foundUser == null) {
            return null;
        }

        User userWithSameUsername = this.findByUsername(user.getUsername());
        if (userWithSameUsername != null && userWithSameUsername.getId() != id) {
            return null;
        }

        foundUser.setUsername(user.getUsername());
        foundUser.setName(user.getName());
        foundUser.setSurname(user.getSurname());

        log.info("IN update - user with id = {} updated", user.getId());

        return userRepository.save(foundUser);
    }

    @Override
    public User updateRole(Long id, String role) {
        User foundUser = this.getById(id);
        if(foundUser == null) {
            return null;
        }

        Role foundRole = roleRepository.findByName(role);
        if(foundRole == null) {
            return null;
        }
        List<Role> roles = new ArrayList<>();
        roles.add(foundRole);

        foundUser.setRoles(roles);
        log.info("IN updateRole - user with id = {} updated", foundUser.getId());
        return userRepository.save(foundUser);
    }

    @Override
    public User addPosition(Long id, Position position) {
        User foundUser = this.getById(id);
        if(foundUser == null) {
            return null;
        }

        foundUser.getPosition().add(position);

        log.info("IN addPosition - {} position added to user {}", position.getName(), foundUser.getId());
        return userRepository.save(foundUser);
    }

    @Override
    public User deletePosition(Long id, Position position) {
        User foundUser = this.getById(id);
        if(foundUser == null) {
            return null;
        }

        boolean result = foundUser.getPosition().remove(position);

        log.info("IN deletePosition - {} position deleted - {}", position.getName(), result);
        return userRepository.save(foundUser);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id).orElse(null);
        user.setPosition(new ArrayList<>());
        user.setRoles(new ArrayList<>());
        userRepository.save(user);
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted", id);
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return this.findByUsername(auth.getName());
    }

    @Override
    public boolean isAdmin(User user) {
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        return user.getRoles().contains(roleAdmin);
    }

}
