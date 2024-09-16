package io.github.tozydev.tasktracker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private static final Path DATA_PATH = Path.of("tasks.json");

    private final List<Task> tasks;

    public TaskManager() throws IOException {
        if (!Files.exists(DATA_PATH)) {
            Files.writeString(DATA_PATH, "[]");
        }

        this.tasks = loadTasks().stream().sorted(Comparator.comparing(Task::id)).collect(Collectors.toList());
    }

    private static List<Task> loadTasks() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(DATA_PATH)) {
            return Arrays.stream(GSON.fromJson(reader, Task[].class)).toList();
        }
    }

    private static void writeTasks(List<Task> tasks) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(DATA_PATH)) {
            writer.write(GSON.toJson(tasks));
        }
    }

    private static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public List<Task> tasksSortedByStatus() {
        return tasks.stream().sorted(Comparator.comparing(Task::status)).toList();
    }

    public int addTask(String description) {
        Task task = new Task(getLastTaskId() + 1, description);
        tasks.add(task);
        return task.id();
    }

    private int getLastTaskId() {
        return tasks.isEmpty() ? 0 : tasks.getLast().id();
    }

    public boolean updateTask(Integer id, String description) {
        var task = findTaskById(id);
        if (task == null) {
            return false;
        }

        task.description(description).updatedAt(now());
        return true;
    }

    public boolean deleteTask(Integer id) {
        return tasks.removeIf(t -> t.id().equals(id));
    }

    public boolean updateTaskStatus(Integer id, TaskStatus status) {
        var task = findTaskById(id);
        if (task == null) {
            return false;
        }

        task.status(status).updatedAt(now());
        return true;
    }

    private Task findTaskById(Integer id) {
        return tasks.stream().filter(t -> t.id().equals(id)).findFirst().orElse(null);
    }

    public List<Task> tasks(TaskStatus status) {
        return tasks.stream().filter(t -> t.status().equals(status)).toList();
    }

    public void save() throws IOException {
        writeTasks(tasks);
    }
}
