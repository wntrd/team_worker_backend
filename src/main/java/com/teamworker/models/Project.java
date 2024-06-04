package com.teamworker.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Project {

    @Id
    @Column(name = "project_id")
    private Long id;

    @Column(name = "project_name")
    private String name;

    @Column(name = "is_finished")
    private Boolean isFinished;

    @Column(name = "create_time")
    private Date createTime;

    @ManyToOne
    @JoinColumn(name = "project_stage_id", nullable = false)
    ProjectStage projectStage;

    @ManyToOne
    @JoinColumn(name = "project_type_id", nullable = false)
    ProjectType projectType;

    @ManyToMany(mappedBy = "projects")
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<Task> tasks;
}
