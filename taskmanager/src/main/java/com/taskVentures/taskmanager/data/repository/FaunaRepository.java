package com.taskVentures.taskmanager.data.repository;

import com.faunadb.client.FaunaClient;
import com.faunadb.client.errors.NotFoundException;
import com.faunadb.client.query.Expr;
import com.faunadb.client.query.Language;
import com.faunadb.client.types.Value;
import com.taskVentures.taskmanager.data.models.Task;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.faunadb.client.query.Language.*;

import java.lang.Class;

public abstract class FaunaRepository<T extends Task> implements Repository<T>, IdentityFactory {

    @Autowired
    protected FaunaClient faunaClient;

    protected final Class<T> entityType;
    protected final String collectionName;
    protected final String collectionIndexName;


    protected FaunaRepository(Class<T> entityType, String collectionName, String collectionIndexName) {
        this.entityType = entityType;
        this.collectionName = collectionName;
        this.collectionIndexName = collectionIndexName;
    }


    @Override
    public CompletableFuture<String> nextId() {

        CompletableFuture<String> result =
                faunaClient.query(
                        NewId()
                )
                        .thenApply(value -> value.to(String.class).get());

        return result;
    }


    @Override
    public CompletableFuture<T> save(T entity) {
        CompletableFuture<T> result =
                faunaClient.query(
                        saveQuery(Language.Value(entity.getId()), Value(entity))
                )
                        .thenApply(this::toEntity);

        return result;
    }

    @Override
    public CompletableFuture<Optional<T>> remove(String id) {
        CompletableFuture<T> result =
                faunaClient.query(
                        Select(
                                Value("data"),
                                Delete(Ref(Collection(collectionName), Value(id)))
                        )
                )
                        .thenApply(this::toEntity);

        CompletableFuture<Optional<T>> optionalResult = toOptionalResult(result);

        return optionalResult;
    }


    @Override
    public CompletableFuture<Optional<T>> find(String id) {
        CompletableFuture<T> result =
                faunaClient.query(
                        Select(
                                Value("data"),
                                Get(Ref(Collection(collectionName), Value(id)))
                        )
                )
                        .thenApply(this::toEntity);

        CompletableFuture<Optional<T>> optionalResult = toOptionalResult(result);

        return optionalResult;
    }




    protected Expr saveQuery(Expr id, Expr data) {
        Expr query =
                Select(
                        Value("data"),
                        If(
                                Exists(Ref(Collection(collectionName), id)),
                                Replace(Ref(Collection(collectionName), id), Obj("data", data)),
                                Create(Ref(Collection(collectionName), id), Obj("data", data))
                        )
                );

        return query;
    }

    protected T toEntity(Value value) {
        return value.to(entityType).get();
    }


    protected CompletableFuture<Optional<T>> toOptionalResult(CompletableFuture<T> result) {
        CompletableFuture<Optional<T>> optionalResult =
                result.handle((v, t) -> {
                    CompletableFuture<Optional<T>> r = new CompletableFuture<>();
                    if(v != null) r.complete(Optional.of(v));
                    else if(t != null && t.getCause() instanceof NotFoundException) r.complete(Optional.empty());
                    else r.completeExceptionally(t);
                    return r;
                }).thenCompose(Function.identity());

        return optionalResult;
    }


}
