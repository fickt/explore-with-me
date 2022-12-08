package ru.yandex.project.service.exception;

public enum Message {
    USER_NOT_FOUND("User with id=%s was not found."),
    EVENT_NOT_FOUND("Event with id=%s was not found."),
    CATEGORY_NOT_FOUND("Category with id=%s was not found."),
    REQUEST_NOT_FOUND("Request with id=%s was not found."),
    COMPILATION_NOT_FOUND("Compilation with id=%s was not found."),
    RATING_NOT_FOUND("Rating with id=%s was not found."),
    NOT_REQUEST_OWNER("This is not your request to be able to cancel it"),
    NOT_INITIATOR("You are not initiator of event"),
    UNAVAILABLE_EVENT("Event is unavailable yet"),
    INVALID_STATUS_TO_CHANGE("Only pending or canceled events can be changed"),
    EVENT_NOT_CONDUCTED("Event with id=%s has not been conducted yet"),
    UNKNOWN_SORT_TYPE("Unknown sort type: %s"),
    NOT_PARTICIPATOR("User with id=%s does not have participation request in this event or it was not approved yet");

    private final String text;

    Message(String text) {
        this.text = text;
    }

    public String get() {
        return this.text;
    }
}
