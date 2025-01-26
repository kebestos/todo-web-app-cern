package ch.cern.todo.model;

import java.util.Date;

public record TaskQuery(String name, String description, Date deadline, Long categoryId) {
}
