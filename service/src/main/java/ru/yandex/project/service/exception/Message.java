package ru.yandex.project.service.exception;

public enum Message {
    USER_NOT_FOUND("User with id=%s was not found."),
    EVENT_NOT_FOUND("Event with id=%s was not found."),
    CATEGORY_NOT_FOUND("Category with id=%s was not found."),
    REQUEST_NOT_FOUND("Request with id=%s was not found."),
    COMPILATION_NOT_FOUND("Compilation with id=%s was not found."),
    NOT_REQUEST_OWNER("This is not your request to be able to cancel it"),
    NOT_INITIATOR("You are not initiator of event"),
    UNAVAILABLE_EVENT("Event is unavailable yet"),
    INVALID_STATUS_TO_CHANGE("Only pending or canceled events can be changed");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return this.message;
    }
}
