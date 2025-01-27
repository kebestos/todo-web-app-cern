package ch.cern.todo.domain;

import java.util.Date;

public record TaskQuery(String name, String description, Date deadline, Long categoryId, Long userId) {
}
