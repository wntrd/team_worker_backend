package com.teamworker.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
public class Task {

    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_name")
    private String name;

    private String description;

    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "due_time")
    private Date dueTime;

    @LastModifiedDate
    @Column(name = "last_edit_time")
    private Date lastEditTime;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id",nullable = false)
    private User assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id",nullable = false)
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id",nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_id",nullable = false)
    private Priority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id",nullable = false)
    private TaskStage stage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_type_id",nullable = false)
    private TaskType type;
}
