package com.teamworker.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
public class Task {

    @Id
    @Column(name = "task_id")
    private Long id;

    @Column(name = "task_name")
    private String name;

    private String description;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "due_time")
    private Date dueTime;

    @Column(name = "last_edit_time")
    private Date lastEditTime;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @ManyToOne
    @JoinColumn(name = "assignee_id",nullable = false)
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "creator_id",nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "project_id",nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "priority_id",nullable = false)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "stage_id",nullable = false)
    private TaskStage stage;

    @ManyToOne
    @JoinColumn(name = "task_type_id",nullable = false)
    private TaskType type;
}
