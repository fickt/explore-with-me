package ru.yandex.project.service.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.project.service.event.model.Event;
import ru.yandex.project.service.user.dto.UserDto;
import ru.yandex.project.service.user.dto.UserShortDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "USER_TABLE")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "EMAIL", unique = true)
    private String email;

    public UserDto toDto() {
        var userDto = new UserDto();
        userDto.setId(id);
        userDto.setName(name);
        userDto.setEmail(email);
        return userDto;
    }

    public UserShortDto toShortDto() {
        var userShortDto = new UserShortDto();
        userShortDto.setId(id);
        userShortDto.setName(name);
        return userShortDto;
    }

}
