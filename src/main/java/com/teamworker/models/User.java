package com.teamworker.models;

import lombok.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_surname")
    private String surname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="position_id")
    private Position position;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "users_projects",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects;

    @OneToMany(mappedBy = "assignee")
    private List<Task> assignedTasks;

    @OneToMany(mappedBy = "creator")
    private List<Task> createdTasks;
}
