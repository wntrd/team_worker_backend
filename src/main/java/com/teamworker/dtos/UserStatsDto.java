package com.teamworker.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamworker.models.Position;
import com.teamworker.models.User;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserStatsDto {

    private String username;
    private String name;
    private String surname;
    private List<Position> position;
    private Integer percentageOnTime;
    private Integer totalCompletedTasks;

    public static UserStatsDto fromUserWithStats(User user, List<Integer> stats) {
        UserStatsDto userStatsDto = new UserStatsDto();
        userStatsDto.setUsername(user.getUsername());
        userStatsDto.setName(user.getName());
        userStatsDto.setSurname(user.getSurname());
        userStatsDto.setPosition(user.getPosition());
        userStatsDto.setPercentageOnTime(stats.get(0));
        userStatsDto.setTotalCompletedTasks(stats.get(1));
        return userStatsDto;
    }
}
