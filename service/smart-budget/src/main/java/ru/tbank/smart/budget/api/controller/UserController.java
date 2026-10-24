package ru.tbank.smart.budget.api.controller;

import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.tbank.smart.budget.api.UsersApi;
import ru.tbank.smart.budget.dao.user.entity.User;
import ru.tbank.smart.budget.model.UserCreateDto;
import ru.tbank.smart.budget.model.UserDto;
import ru.tbank.smart.budget.service.UserService;

@RestController
public class UserController implements UsersApi {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Override
  public ResponseEntity<UserDto> createUser(UserCreateDto userCreateDto) {
    User user = userService.createUser(userCreateDto);
    UserDto createdUser = mapToDto(user);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdUser.getId())
            .toUri();
    return ResponseEntity.created(location).body(createdUser);
  }

  @Override
  public ResponseEntity<UserDto> getCurrentUser() {
    User user = userService.getCurrentUser();
    return ResponseEntity.ok(mapToDto(user));
  }

  @Override
  @PreAuthorize("@securityService.isOwner(#id)")
  public ResponseEntity<UserDto> getUserById(UUID id) {
    User user = userService.getUserById(id);
    return ResponseEntity.ok(mapToDto(user));
  }

  private static UserDto mapToDto(User user) {
    return new UserDto(user.getId(), user.getEmail(), user.getFio());
  }
}
