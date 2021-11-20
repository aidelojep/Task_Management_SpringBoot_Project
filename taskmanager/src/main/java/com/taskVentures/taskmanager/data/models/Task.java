package com.taskVentures.taskmanager.data.models;

import com.faunadb.client.types.FaunaConstructor;
import com.faunadb.client.types.FaunaField;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Task {
    private String id;

    @FaunaField
    private String name;

    @FaunaField
    private String description;

    @FaunaField
    private  boolean isCompleted;

    @FaunaConstructor
    public Task(@FaunaField("id")String id, @FaunaField("name")String name, @FaunaField("description")String description, @FaunaField("isCompleted")boolean isCompleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isCompleted = isCompleted;
    }

}
