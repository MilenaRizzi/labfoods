package com.senai.labfoods.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.senai.labfoods.dtos.LoginUserDto;
import com.senai.labfoods.dtos.RecoveryJwtTokenDto;
import com.senai.labfoods.enums.RoleName;
import com.senai.labfoods.exception.AuthorizationFailedException;
import com.senai.labfoods.exception.ExistingRecordException;
import com.senai.labfoods.exception.RecordNotFoundException;
import com.senai.labfoods.exception.UserNotFoundException;
import com.senai.labfoods.model.Role;
import com.senai.labfoods.model.User;
import com.senai.labfoods.repository.RecipeRepository;
import com.senai.labfoods.repository.UserRepository;
import com.senai.labfoods.security.authentication.JwtTokenService;
import com.senai.labfoods.security.userdetails.UserDetailsImpl;

import jakarta.transaction.Transactional;

@Service
public class UserService {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenService jwtTokenService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RecipeRepository recipeRepository;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
    try {
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
          loginUserDto.getEmail(), loginUserDto.getPassword());

      Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
      return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    } catch (Exception e) {
      throw new AuthorizationFailedException("Email ou senha inválidos.");
    }
  }

  public List<User> consult() {
    return userRepository.findAll();
  }

  public long countUsers() {
    return userRepository.count();
  }

  public User getUserId(UUID id) {
    Optional<User> user = this.userRepository.findById(id);
    return user.orElseThrow(() -> new RecordNotFoundException("Usuário não encontrado com código informado"));
  }

  @Transactional
  public User create(User user) {
    if (userRepository.existsByCpf(user.getCpf())) {
      throw new ExistingRecordException("Usuário com cpf já cadastrado");
    }
    if (userRepository.existsByEmail(user.getEmail())) {
      throw new ExistingRecordException("Usuário com email já cadastrado");
    }
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    user.setRoles(List.of(Role.builder().name(RoleName.ROLE_USER).build()));
    return userRepository.save(user);
  }

  public User update(String token, User user) {
    if (!checkAuthorization(token, user.getId())) {
      throw new AuthorizationFailedException("Não é possível editar conta de outro usuário");
    }
    var userBD = this.getUserId(user.getId());
    userBD.setName(user.getName());
    userBD.setGender(user.getGender());
    userBD.setAddress(user.getAddress());
    userBD.setEmail(user.getEmail());
    userBD.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userBD.setDateOfBirth(user.getDateOfBirth());
    return userRepository.save(userBD);
  }

  public void delete(String token, UUID id) {
    boolean exists = userRepository.existsById(id);
    if (!exists) {
      throw new RecordNotFoundException("Usuário não encontrado");
    }

    if (!checkAuthorization(token, id)) {
      throw new AuthorizationFailedException("Não é possível excluir conta de outro usuário");
    }

    long recipeCount = recipeRepository.countByUserId(id);
    if (recipeCount > 0) {
      throw new ExistingRecordException("Não é possível atualizar o usuário pois há receitas relacionadas");
    }

    userRepository.deleteById(id);
  }

  private boolean checkAuthorization(String token, UUID id) {
    User loggedUser = getUserByToken(token);
    return loggedUser.getId().equals(id);
  }

  public User getUserByToken(String token) {
    String email = jwtTokenService.getSubjectFromToken(token.replace("Bearer ", ""));
    return userRepository.findByEmail(email)
        .orElseThrow(UserNotFoundException::new);
  }
}
