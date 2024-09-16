package io.github.tozydev.tasktracker;

public enum TaskStatus {
    TODO("todo"), IN_PROGRESS("in progress"), DONE("done");

    private final String label;

    TaskStatus(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
