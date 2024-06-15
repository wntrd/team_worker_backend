package com.teamworker.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.io.Serializable;

public enum ProjectType {
    MOBILE,
    DESKTOP,
    WEB,
    SERVER,
    OTHER;
}
