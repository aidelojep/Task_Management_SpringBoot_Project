package com.taskVentures.taskmanager.data.payloads.requests;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequest {
    private String name;

    private String description;

    private  boolean isCompleted;
}
