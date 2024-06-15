package com.teamworker.services.impl;

import com.teamworker.models.*;
import com.teamworker.models.enums.TaskStage;
import com.teamworker.repositories.RoleRepository;
import com.teamworker.repositories.TaskRepository;
import com.teamworker.services.TaskService;
import com.teamworker.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm");


    @Override
    public Task add(Task task) throws ParseException {

        if (dateFormat.parse(task.getDueTime()).before(dateFormat.parse(task.getCreateTime()))) {
            return null;
        }

        task.setOverdue(false);

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
    public List<Task> getAll() throws ParseException {
        List<Task> tasks = taskRepository.findAll();
        for (Task task : tasks) {
            if (dateFormat.parse(task.getDueTime()).before(new Date())) {
                task.setOverdue(true);
                taskRepository.save(task);
            }
        }
        log.info("IN getAll - {} tasks added", tasks.size());
        return tasks;
    }

    @Override
    public List<Task> getAllByStage(String stageName) throws ParseException {
        List<Task> tasks = taskRepository.getAllByAssigneeAndStage(userService.getCurrentUser(), TaskStage.valueOf(stageName));
        for (Task task : tasks) {
            if (dateFormat.parse(task.getDueTime()).before(new Date())) {
                task.setOverdue(true);
                taskRepository.save(task);
            }
        }
        log.info("IN getAllByStage - {} tasks added", tasks.size());
        return tasks;
    }

    @Override
    public List<Task> getAllByStageForAdmin(String stageName) throws ParseException {
        List<Task> tasks = taskRepository.getAllByStage(TaskStage.valueOf(stageName));
        for (Task task : tasks) {
            if (dateFormat.parse(task.getDueTime()).before(new Date())) {
                task.setOverdue(true);
                taskRepository.save(task);
            }
        }
        log.info("IN getAllByStageForAdmin - {} tasks added", tasks.size());
        return tasks;
    }

    @Override
    public Task changeStage(Long taskId, String stageName) {
        Task task = taskRepository.findById(taskId).orElse(null);

        if (task == null) {
            return null;
        }

        if (Objects.equals(stageName, TaskStage.IN_PROGRESS.name())) {
            task.setStartTime(dateFormat.format(new Date()));
        }
        else if (Objects.equals(stageName, TaskStage.ON_REVIEW.name())) {
            task.setEndTime(dateFormat.format(new Date()));
        }

        task.setStage(TaskStage.valueOf(stageName));
        taskRepository.save(task);
        log.info("IN changeStage - task with id {} updated", task.getId());
        return task;
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
    public Task update(Long id, Task task) throws ParseException {
        Task foundTask = taskRepository.findById(id).orElse(null);
        if(foundTask == null) {
            return null;
        }

//        if (foundTask.getCreator() != userService.getCurrentUser() ||
//                !(userService.isAdmin(userService.getCurrentUser()) &&
//                        userService.isAdminOfProject(userService.getCurrentUser(), foundTask.getProject()))) {
//            return null;
//        }

        if (dateFormat.parse(task.getDueTime()).before(dateFormat.parse(task.getCreateTime()))) {
            return null;
        }

        foundTask.setName(task.getName());
        foundTask.setDescription(task.getDescription());
        foundTask.setDueTime(task.getDueTime());
        foundTask.setEndTime(task.getEndTime());
        foundTask.setAssignee(task.getAssignee());
        foundTask.setProject(task.getProject());
        foundTask.setPriority(task.getPriority());
        foundTask.setType(task.getType());
        foundTask.setLastEditTime(task.getLastEditTime());

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
