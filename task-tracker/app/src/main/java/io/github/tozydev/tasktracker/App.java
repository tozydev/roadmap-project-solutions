package io.github.tozydev.tasktracker;

public class App implements AutoCloseable {
    private final String[] args;
    private final String command;
    private final TaskManager manager;

    public App(String[] args, TaskManager manager) {
        this.args = args;
        this.command = argumentOrNull(0);
        this.manager = manager;
    }

    public static void main(String[] args) throws Exception {
        try (var app = new App(args, new TaskManager())) {
            app.run();
        }
    }

    public void run() {
        if (command == null) {
            printHelp();
            return;
        }

        switch (command) {
            case "add", "insert", "a" -> addTask();
            case "update", "modify", "u" -> updateTask();
            case "delete", "remove", "d" -> deleteTask();
            case "mark-in-progress", "in-progress" -> markTaskInProgress();
            case "mark-done", "done" -> markTaskDone();
            case "list", "l" -> listTasks();
            default -> printHelp();
        }
    }

    private void listTasks() {
        var filterStatus = argumentOrNull(1);
        if (filterStatus == null) {
            listAllTasks();
        } else {
            listTasksByStatus(filterStatus);
        }
    }

    private void listTasksByStatus(String filterStatus) {
        try {
            var status = TaskStatus.valueOf(filterStatus.toUpperCase().replace('-', '_'));
            System.out.println("--- Listing all tasks by status: " + status + " ---");
            var tasks = manager.tasks(status);
            if (tasks.isEmpty()) {
                System.out.println("No tasks found");
            }
            for (Task task : tasks) {
                System.out.printf("- %s [%d]\n", task.description(), task.id());
            }
        } catch (IllegalArgumentException ignored) {
            System.err.println("Usage: task-tracker list [todo/in-progress/done]");
        }
    }

    private void listAllTasks() {
        System.out.println("--- Listing all tasks ---");
        var tasks = manager.tasksSortedByStatus();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found");
        }
        for (Task task : tasks) {
            System.out.printf("- %s [%d] (%s)\n", task.description(), task.id(), task.status().label());
        }
    }

    private void markTaskDone() {
        var id = idArgumentOrNull();
        if (id == null) {
            System.err.println("Usage: task-tracker mark-done <id>");
            return;
        }

        if (manager.updateTaskStatus(id, TaskStatus.DONE)) {
            System.out.println("Marked task " + id + " done");
        } else {
            System.err.println("Marked task " + id + " done failed");
        }
    }

    private void markTaskInProgress() {
        var id = idArgumentOrNull();
        if (id == null) {
            System.err.println("Usage: task-tracker mark-in-progress <id>");
            return;
        }

        if (manager.updateTaskStatus(id, TaskStatus.IN_PROGRESS)) {
            System.out.println("Marked task " + id + " in progress");
        } else {
            System.err.println("Marked task " + id + " in progress failed");
        }
    }

    private void deleteTask() {
        var id = idArgumentOrNull();
        if (id == null) {
            System.err.println("Usage: task-tracker delete <id>");
            return;
        }

        if (manager.deleteTask(id)) {
            System.out.println("Task deleted: " + id);
        } else {
            System.err.println("Task could not be deleted: " + id);
        }
    }

    private void updateTask() {
        var id = idArgumentOrNull();
        var description = argumentOrNull(2);
        if (id == null || description == null) {
            System.err.println("Usage: task-tracker update <id> <description>");
            return;
        }

        if (manager.updateTask(id, description)) {
            System.out.println("Updated task " + id + " with description " + description);
        } else {
            System.err.println("Failed to update task " + id + " with description " + description);
        }
    }

    private String argumentOrNull(int index) {
        return args.length > index ? args[index] : null;
    }

    private Integer idArgumentOrNull() {
        return args.length > 1 ? Integer.parseInt(args[1]) : null;
    }

    private void printHelp() {
        System.out.println("Usage: task-tracker <command> <arguments>");
        System.out.println("Available commands:");
        System.out.println("\ttask-tracker help -- Shows this help message");
        System.out.println("\ttask-tracker add <description> -- Adds a task to the list");
        System.out.println("\ttask-tracker update <id> <description> -- Updates a task");
        System.out.println("\ttask-tracker delete <id> -- Deletes a task");
        System.out.println("\ttask-tracker mark-in-progress <id> -- Marks in-progress a task");
        System.out.println("\ttask-tracker mark-done <id> -- Marks done a task");
        System.out.println("\ttask-tracker list [todo/in-progress/done] -- Lists tasks");
    }

    private void addTask() {
        var description = argumentOrNull(1);
        if (description == null) {
            System.err.println("Usage: task-tracker add <description>");
            return;
        }

        var taskId = manager.addTask(description);
        System.out.println("Task added: " + taskId);
    }

    @Override
    public void close() throws Exception {
        manager.save();
    }
}
