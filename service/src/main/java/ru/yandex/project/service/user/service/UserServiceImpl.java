package ru.yandex.project.service.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.project.service.user.dto.UserDto;
import ru.yandex.project.service.user.model.User;
import ru.yandex.project.service.user.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        var user = userDto.toUserEntity();
        return repository.save(user).toDto();
    }

    @Override
    public List<UserDto> getUsers(Long from, Long size, String[] ids) {
        var pageRequest = PageRequest.of(from.intValue() - 1, size.intValue());
        var idsAsLong = convertStringArrayToLongList(ids); //as input ids we get "[1]" as String wtf
        if (!idsAsLong.isEmpty()) {
            return repository.findAllByIdIn(idsAsLong, pageRequest).stream()
                    .map(User::toDto)
                    .collect(Collectors.toList());
        }
        return repository.findAllByIdNotNull(pageRequest).stream()
                .map(User::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        repository.deleteById(userId);
    }

    /**
     * As @RequestParam Long[] ids of endpoint /users we get ["[value]"] instead of [value],
     * so via this method we extract data from mess like "[]" etc
     *
     * @param arr array of id of users to be found
     * @return List of id converted from "[value]" to Long value
     */
    private List<Long> convertStringArrayToLongList(String[] arr) {
        return arr == null ? Collections.emptyList() : Arrays
                .stream(arr)
                .map(o -> o.replaceAll("\\W", ""))
                .filter(o -> !o.isBlank())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
