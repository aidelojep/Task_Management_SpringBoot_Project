package com.taskVentures.taskmanager.services;

import com.taskVentures.taskmanager.data.models.Task;
import com.taskVentures.taskmanager.data.payloads.requests.TaskRequest;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface TaskService {
    CompletableFuture<Task> createTask(TaskRequest taskRequest);
    CompletableFuture<Optional<Task>> updateTask(String id, TaskRequest taskRequest);
    CompletableFuture<Optional<Task>> deleteTask(String id);
    CompletableFuture<Optional<Task>> getTask(String id);


}
