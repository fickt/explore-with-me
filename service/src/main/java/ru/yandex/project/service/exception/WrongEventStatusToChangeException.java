package ru.yandex.project.service.exception;

import static ru.yandex.project.service.exception.Message.INVALID_STATUS_TO_CHANGE;

public class WrongEventStatusToChangeException extends RuntimeException {

    public WrongEventStatusToChangeException() {
        super(INVALID_STATUS_TO_CHANGE.get());
    }
}
