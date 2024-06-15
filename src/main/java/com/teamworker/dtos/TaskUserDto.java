package com.teamworker.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamworker.models.Position;
import com.teamworker.models.Role;
import com.teamworker.models.User;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskUserDto {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private List<Position> position;
    private List<Role> roles;

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setName(name);
        user.setSurname(surname);
        user.setPosition(position);
        user.setRoles(roles);
        return user;
    }

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setSurname(user.getSurname());
        userDto.setPosition(user.getPosition());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
