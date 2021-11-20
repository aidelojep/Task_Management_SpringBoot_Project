package com.taskVentures.taskmanager.web;

import com.taskVentures.taskmanager.data.payloads.requests.TaskRequest;
import com.taskVentures.taskmanager.services.TaskService;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
@ApiResponses(value = {
        @io.swagger.annotations.ApiResponse(code = 400, message = "This is a bad request, please follow the API documentation for the proper request format"),
        @io.swagger.annotations.ApiResponse(code = 401, message = "Due to security constraints, your access request cannot be authorized"),
        @io.swagger.annotations.ApiResponse(code = 500, message = "The server is down. Please bear with us."),
})
public class TaskController {
    TaskService taskService;

    @PostMapping("/create")
    public CompletableFuture<?> createTask(@RequestBody TaskRequest taskRequest) {
        return taskService.createTask(taskRequest)
                .thenApply(todoEntity -> new ResponseEntity<>(todoEntity, HttpStatus.CREATED));
    }


    @GetMapping("/get/{id}")
    public CompletableFuture<?> getTask(@PathVariable("id") String id) {
        CompletableFuture<ResponseEntity> result =
                taskService.getTask(id)
                        .thenApply(optionalTodoEntity ->
                                optionalTodoEntity
                                        .map(todoEntity -> new ResponseEntity<>(todoEntity, HttpStatus.OK))
                                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND))
                        );
        return result;
    }


    @PutMapping("/update/{id}")
    public CompletableFuture<?> updateTask(@PathVariable("id") String id, @RequestBody TaskRequest taskRequest) {
        CompletableFuture<ResponseEntity> result =
                taskService.updateTask(id, taskRequest)
                        .thenApply(optionalTodoEntity ->
                                optionalTodoEntity
                                        .map(todoEntity -> new ResponseEntity<>(todoEntity, HttpStatus.OK))
                                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)
                                        )
                        );
        return result;
    }

    @DeleteMapping(value = "/delete/{id}")
    public CompletableFuture<?> deleteTask(@PathVariable("id")String id) {
        CompletableFuture<ResponseEntity> result =
                taskService.deleteTask(id)
                        .thenApply(optionalTodoEntity ->
                                optionalTodoEntity
                                        .map(todo -> new ResponseEntity<>(todo, HttpStatus.OK))
                                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)
                                        )
                        );
        return result;
    }



}
