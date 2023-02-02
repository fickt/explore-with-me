package ru.yandex.project.service.exception;

public class EventIsNotConductedException extends RuntimeException {
    public EventIsNotConductedException(String message) {
        super(message);
    }
}
