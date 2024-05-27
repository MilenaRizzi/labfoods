package com.senai.labfoods.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.senai.labfoods.dtos.DashboardResponseDto;
import com.senai.labfoods.dtos.LoginUserDto;
import com.senai.labfoods.dtos.RecipeResponseDto;
import com.senai.labfoods.dtos.RecoveryJwtTokenDto;
import com.senai.labfoods.dtos.UserRequestDto;
import com.senai.labfoods.dtos.UserResponseDto;
import com.senai.labfoods.dtos.UserUpdateRequestDto;
import com.senai.labfoods.model.Recipe;
import com.senai.labfoods.model.User;
import com.senai.labfoods.service.RecipeService;
import com.senai.labfoods.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private RecipeService recipeService;

  @Autowired
  private ModelMapper mapper;

  @PostMapping("/conta")
  public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRequestDto request) {
    User user = mapper.map(request, User.class);
    userService.create(user);
    var resp = mapper.map(user, UserResponseDto.class);
    return ResponseEntity.created(URI.create(user.getId().toString())).body(resp);
  }

  @PostMapping("/login")
  public ResponseEntity<RecoveryJwtTokenDto> authenticateUser(@RequestBody LoginUserDto loginUserDto) {
    RecoveryJwtTokenDto token = userService.authenticateUser(loginUserDto);
    return new ResponseEntity<>(token, HttpStatus.OK);
  }

  @GetMapping("/dashboard")
  public ResponseEntity<DashboardResponseDto> getDashboardData() {
    var users = userService.consult();
    var userDtos = users.stream().map(user -> mapper.map(user, UserResponseDto.class)).toList();
    long totalUsers = userService.countUsers();
    long totalRecipes = recipeService.countRecipes();
    List<Recipe> topVotedRecipes = recipeService.getTopVotedRecipes(3);
    List<RecipeResponseDto> topVotedRecipeDtos = topVotedRecipes.stream()
        .map(recipe -> mapper.map(recipe, RecipeResponseDto.class))
        .toList();
    DashboardResponseDto response = new DashboardResponseDto(totalUsers, totalRecipes, topVotedRecipeDtos, userDtos);
    return ResponseEntity.ok().body(response);
  }

  @GetMapping()
  public ResponseEntity<List<UserResponseDto>> getUsers() {
    var users = userService.consult();
    var resp = users.stream().map(user -> mapper.map(user, UserResponseDto.class)).toList();
    return ResponseEntity.ok().body(resp);
  }

  @GetMapping("{id}")
  public ResponseEntity<UserResponseDto> getUserId(@PathVariable UUID id) {
    var recipe = userService.getUserId(id);
    var resp = mapper.map(recipe, UserResponseDto.class);
    return ResponseEntity.ok().body(resp);
  }

  @PutMapping("/conta/editar/{id}")
  public ResponseEntity<UserResponseDto> update(@RequestHeader("Authorization") String token, @PathVariable UUID id,
      @RequestBody @Valid UserUpdateRequestDto request) {
    var user = mapper.map(request, User.class);
    user.setId(id);
    user = userService.update(token, user);
    var resp = mapper.map(user, UserResponseDto.class);
    return ResponseEntity.ok().body(resp);
  }

  @DeleteMapping("/conta/deletar/{id}")
  public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable UUID id) {
    userService.delete(token, id);
    return ResponseEntity.noContent().build();
  }
}
