package ru.yandex.project.service.user.service;

import ru.yandex.project.service.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    List<UserDto> getUsers(Long from, Long size, String[] ids);

    void deleteUser(Long userId);
}
