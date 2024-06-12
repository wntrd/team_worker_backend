package com.teamworker.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamworker.models.User;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MainUserInfoDto {
    private String username;
    private String password;
    private String name;
    private String surname;

    public User toUser() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        return user;
    }

    public static MainUserInfoDto fromUser(User user) {
        MainUserInfoDto userDto = new MainUserInfoDto();
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setSurname(user.getSurname());
        return userDto;
    }
}
