package com.teamworker.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum ProjectStage {
    STARTED,
    IN_PROGRESS,
    DEBUG,
    RELEASE;
}
