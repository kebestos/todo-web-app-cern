package ch.cern.todo.service.exception;

public enum ExceptionMessage {

    TASK_CATEGORY_NOT_FOUND("Task category not found"),
    TASK_NOT_FOUND("Task not found"),
    USER_NOT_FOUND("User not found"),
    UNAUTHORIZED_ACCESS("Unauthorized access");

    private final String message;

    private ExceptionMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
