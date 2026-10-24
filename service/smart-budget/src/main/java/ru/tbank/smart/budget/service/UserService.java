package ru.tbank.smart.budget.service;

import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tbank.smart.budget.dao.user.entity.User;
import ru.tbank.smart.budget.dao.user.repository.UserRepository;
import ru.tbank.smart.budget.exception.EntityNotFoundException;
import ru.tbank.smart.budget.model.UserCreateDto;

@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final SecurityService securityService;

  public UserService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      SecurityService securityService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.securityService = securityService;
  }

  @Transactional
  public User createUser(UserCreateDto dto) {
    String encodedPassword = passwordEncoder.encode(dto.getPassword());
    User user = new User(dto.getFio(), dto.getEmail(), encodedPassword);
    return userRepository.save(user);
  }

  public User getCurrentUser() {
    String email = securityService.getCurrentEmail();
    return userRepository
        .findByEmail(email)
        .orElseThrow(
            () -> new EntityNotFoundException("Пользователь с email: " + email + " не найден"));
  }

  public User getUserById(UUID id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + id + " не найден"));
  }

  public User getUserReference(UUID id) {
    return userRepository.getReferenceById(id);
  }

  public boolean existsById(UUID id) {
    return userRepository.existsById(id);
  }
}
