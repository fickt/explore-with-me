package ru.yandex.project.service.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.project.service.user.dto.UserDto;
import ru.yandex.project.service.user.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {

    private static final String ENDPOINT_DELETE_USER_ID = "/{userId}";
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        log.info("POST /users userDto: {}", userDto);
        return userService.addUser(userDto);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam("from") Long from,
                                  @RequestParam("size") Long size,
                                  @Nullable @RequestParam("ids") String[] ids) {
        log.info("GET /users from: {}, size: {}, ids: {}", from, size,
                ids == null ? "null" : Arrays.stream(ids).collect(Collectors.toList()));
        return userService.getUsers(from, size, ids);
    }

    @DeleteMapping(ENDPOINT_DELETE_USER_ID)
    public void deleteUser(@PathVariable Long userId) {
        log.info("DELETE /users userId: {}", userId);
        userService.deleteUser(userId);
    }
}
