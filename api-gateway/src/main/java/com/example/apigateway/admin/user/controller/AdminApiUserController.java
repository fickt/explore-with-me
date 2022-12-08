package com.example.apigateway.admin.user.controller;

import com.example.apigateway.admin.user.client.AdminApiUserClient;
import com.example.apigateway.dto.userdto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/users")
public class AdminApiUserController {
    private static final String ENDPOINT_DELETE_USER_BY_ID = "/{userId}";
    private final AdminApiUserClient client;

    @Autowired
    public AdminApiUserController(AdminApiUserClient client) {
        this.client = client;
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Valid UserDto userDto) {
        log.info(String.format("POST /admin/users sent data userDto: %s", userDto));
        return client.addUser(userDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers(@PositiveOrZero @RequestParam(value = "from", defaultValue = "1") Long from,
                                           @Positive @RequestParam(value = "size", defaultValue = "10") Long size,
                                           @RequestParam(value = "ids", required = false) Integer[] ids) {
        log.info(String.format("GET /admin/users sent data from: %s, size: %s, ids: %s", from, size,
                ids != null ? Arrays.stream(ids).collect(Collectors.toList()) : "null"));
        return client.getUsers(from, size, ids);
    }

    @DeleteMapping(ENDPOINT_DELETE_USER_BY_ID)
    public void deleteUser(@PositiveOrZero @PathVariable Long userId) {
        log.info(String.format("DELETE /admin/users sent data userId: %s", userId));
        client.deleteUser(userId);
    }
}
