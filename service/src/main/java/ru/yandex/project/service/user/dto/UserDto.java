package ru.yandex.project.service.user.dto;

import lombok.Data;
import ru.yandex.project.service.user.model.User;


@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;

    public User toUserEntity() {
        var user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
