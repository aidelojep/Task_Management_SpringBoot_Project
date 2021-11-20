package com.taskVentures.taskmanager.data.repository;

import com.taskVentures.taskmanager.data.models.Task;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface Repository<T extends Task> {

    CompletableFuture<T> save(T entity);

    CompletableFuture<Optional<T>> find(String id);

    CompletableFuture<List<T>> findAll();

    CompletableFuture<Optional<T>> remove(String id);
}
