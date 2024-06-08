package com.teamworker.dtos;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String username;
    private String password;
}
