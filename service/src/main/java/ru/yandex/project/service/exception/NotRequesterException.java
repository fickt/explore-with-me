package ru.yandex.project.service.exception;

public class NotRequesterException extends RuntimeException {

    public NotRequesterException(String message) {
        super(message);
    }
}
