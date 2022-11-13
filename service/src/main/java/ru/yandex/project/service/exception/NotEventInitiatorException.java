package ru.yandex.project.service.exception;

import static ru.yandex.project.service.exception.Message.NOT_INITIATOR;

public class NotEventInitiatorException extends RuntimeException {

    public NotEventInitiatorException() {
        super(NOT_INITIATOR.get());
    }
}
