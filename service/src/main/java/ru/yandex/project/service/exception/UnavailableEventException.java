package ru.yandex.project.service.exception;

import static ru.yandex.project.service.exception.Message.UNAVAILABLE_EVENT;

public class UnavailableEventException extends RuntimeException {

    public UnavailableEventException() {
        super(UNAVAILABLE_EVENT.get());
    }
}
