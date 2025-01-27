package ch.cern.todo.service.exception;

public class TaskCreationFailed extends RuntimeException {
    public TaskCreationFailed(String message) {
        super(message);
    }
}
