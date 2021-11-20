package com.taskVentures.taskmanager.data.repository;

import java.util.concurrent.CompletableFuture;

public interface IdentityFactory {

    CompletableFuture<String> nextId();
}
