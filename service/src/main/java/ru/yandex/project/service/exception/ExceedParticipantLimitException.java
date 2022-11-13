package ru.yandex.project.service.exception;

public class ExceedParticipantLimitException extends RuntimeException {

    public ExceedParticipantLimitException() {
        super("Participant limit is exceeded");
    }
}
