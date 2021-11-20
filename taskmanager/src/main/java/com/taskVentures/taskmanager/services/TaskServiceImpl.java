package com.taskVentures.taskmanager.services;

import com.taskVentures.taskmanager.data.models.Task;
import com.taskVentures.taskmanager.data.payloads.requests.TaskRequest;
import com.taskVentures.taskmanager.data.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class TaskServiceImpl  implements  TaskService  {
    private final TaskRepository taskRepository;

    @Override
    public CompletableFuture<Task> createTask(TaskRequest taskRequest) {
        CompletableFuture<Task> newTask = taskRepository.nextId()
                .thenApply(id -> new Task(id, taskRequest.getName(), taskRequest.getDescription(), taskRequest.isCompleted())).thenCompose(taskRepository::save);
     return newTask;
    }

    @Override
    public CompletableFuture<Optional<Task>> getTask(String id) {
        return taskRepository.find(id);
    }

    @Override
    public CompletableFuture<Optional<Task>> updateTask(String id, TaskRequest taskRequest) {
        CompletableFuture<Optional<Task>> result =
                taskRepository.find(id)
                        .thenCompose(optionalTodoEntity ->
                                optionalTodoEntity
                                        .map(todoEntity -> taskRepository.save(new Task(id, taskRequest.getName(), taskRequest.getDescription(),  taskRequest.isCompleted())).thenApply(Optional::of))
                                        .orElseGet(() -> CompletableFuture.completedFuture(Optional.empty())));

        return result;
    }

    @Override
    public CompletableFuture<Optional<Task>> deleteTask(String id) {
        return taskRepository.remove(id);
    }



}
