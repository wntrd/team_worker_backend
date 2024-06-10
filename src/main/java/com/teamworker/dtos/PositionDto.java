package com.teamworker.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamworker.models.Position;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionDto {

    private Long id;
    private String name;

    public Position toPosition() {
        Position position = new Position();
        position.setId(id);
        position.setName(name);
        return position;
    }

    public static PositionDto fromPosition(Position position) {
        PositionDto positionDto = new PositionDto();
        positionDto.setId(position.getId());
        positionDto.setName(position.getName());
        return positionDto;
    }
}
