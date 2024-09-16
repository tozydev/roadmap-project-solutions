package io.github.tozydev.tasktracker;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private Integer id;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(Integer id, String description, TaskStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Task(Integer id, String description) {
        this(id, description, TaskStatus.TODO, LocalDateTime.now(), null);
    }

    public Integer id() {
        return id;
    }

    public Task id(Integer id) {
        this.id = id;
        return this;
    }

    public String description() {
        return description;
    }

    public Task description(String description) {
        this.description = description;
        return this;
    }

    public TaskStatus status() {
        return status;
    }

    public Task status(TaskStatus status) {
        this.status = status;
        return this;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public Task createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public Task updatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(description, task.description) && status == task.status && Objects.equals(createdAt, task.createdAt) && Objects.equals(updatedAt, task.updatedAt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(status);
        result = 31 * result + Objects.hashCode(createdAt);
        result = 31 * result + Objects.hashCode(updatedAt);
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
