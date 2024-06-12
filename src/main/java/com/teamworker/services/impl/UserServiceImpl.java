package com.teamworker.services.impl;

import com.teamworker.models.Position;
import com.teamworker.models.Project;
import com.teamworker.models.Role;
import com.teamworker.models.User;
import com.teamworker.models.enums.Status;
import com.teamworker.repositories.RoleRepository;
import com.teamworker.repositories.UserRepository;
import com.teamworker.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
        log.info("IN getAll - {} users found", users.size());
        return users;
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
        User foundUser = userRepository.findById(id).orElse(null);
        if(foundUser == null) {
            return null;
        }

        User userWithSameUsername = findByUsername(user.getUsername());

        if (userWithSameUsername != null && userWithSameUsername.getId() != id) {
            return null;
        }

        foundUser.setUsername(user.getUsername());
        foundUser.setPassword(passwordEncoder.encode(user.getPassword()));
        foundUser.setName(user.getName());
        foundUser.setSurname(user.getSurname());

        log.info("IN update - {} project updated", user.getId());

        return userRepository.save(foundUser);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted", id);
    }
}
