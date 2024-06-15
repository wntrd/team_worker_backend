package com.teamworker.services.impl;

import com.teamworker.models.*;
import com.teamworker.repositories.RoleRepository;
import com.teamworker.repositories.TaskRepository;
import com.teamworker.services.TaskService;
import com.teamworker.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    @Override
    public Task add(Task task) {
        task.setCreateTime(new Timestamp(System.currentTimeMillis()));
        task.setLastEditTime(new Timestamp(System.currentTimeMillis()));
        log.info("IN add - {} task added", task.getName());
        return taskRepository.save(task);
    }

    @Override
    public Task getById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            log.warn("IN getById - no task found by id: {}", id);
            return null;
        }

        log.info("IN getById - {} task added", task.getId());
        return task;
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = taskRepository.findAll();
        log.info("IN getAll - {} tasks added", tasks.size());
        return tasks;
    }

    //таски проектів адміна
//    @Override
//    public List<Task> getAll() {
//        User currentUser = userService.getCurrentUser();
//        List<Position> currentUserPositions = currentUser.getPosition();
//
//        List<Task> tasks = new ArrayList<>();
//        List<Project> projects = new ArrayList<>();
//        for (Position position : currentUserPositions) {
//            projects.add(position.getProject());
//        }
//        List<Project> projectsWithoutDuplicates = new ArrayList<>(new HashSet<>(projects));
//        for (Project project : projectsWithoutDuplicates) {
//            tasks.addAll(this.getAllByProject(project));
//        }
//        log.info("IN getAll - {} tasks found", tasks.size());
//        return tasks;
//    }

    @Override
    public Task update(Long id, Task task) {
        Task foundTask = taskRepository.findById(id).orElse(null);
        if(foundTask == null) {
            return null;
        }
        if (foundTask.getCreator() != userService.getCurrentUser() ||
                !(userService.isAdmin(userService.getCurrentUser()) &&
                        userService.isAdminOfProject(userService.getCurrentUser(), foundTask.getProject()))) {
            return null;
        }

        foundTask.setName(task.getName());
        foundTask.setDescription(task.getDescription());
        foundTask.setDueTime(task.getDueTime());
        foundTask.setStartTime(task.getStartTime());
        foundTask.setEndTime(task.getEndTime());
        foundTask.setAssignee(task.getAssignee());
        foundTask.setProject(task.getProject());
        foundTask.setPriority(task.getPriority());
        foundTask.setStage(task.getStage());
        foundTask.setType(task.getType());
        foundTask.setLastEditTime(new Timestamp(System.currentTimeMillis()));

        log.info("IN update - {} task updated", task.getId());

        return taskRepository.save(foundTask);
    }

    @Override
    public void delete(Long id) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            log.warn("IN delete - no task found by id: {}", id);
            return;
        }

        taskRepository.deleteById(id);
        log.info("IN delete - task with id: {} successfully deleted", id);
    }

    @Override
    public List<Task> getAllByProject(Project project) {
        List<Task> tasks = taskRepository.getAllByProject(project);
        log.info("IN getAllByProject - {} tasks found", tasks.size());
        return tasks;
    }

}
