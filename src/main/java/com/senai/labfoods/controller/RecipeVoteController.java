package com.senai.labfoods.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.senai.labfoods.dtos.RecipeVoteRequestDto;
import com.senai.labfoods.dtos.RecipeVoteResponseDto;
import com.senai.labfoods.model.RecipeVote;
import com.senai.labfoods.service.RecipeVoteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vote")
public class RecipeVoteController {

  @Autowired
  private RecipeVoteService recipeVoteService;

  @Autowired
  private ModelMapper mapper;

  @PostMapping()
  public ResponseEntity<RecipeVoteResponseDto> insert(@RequestHeader("Authorization") String token,
      @RequestBody @Valid RecipeVoteRequestDto request) {
    RecipeVote recipeVote = mapper.map(request, RecipeVote.class);
    recipeVoteService.create(token, recipeVote);
    var resp = mapper.map(recipeVote, RecipeVoteResponseDto.class);
    return ResponseEntity.created(URI.create(recipeVote.getId().toString())).body(resp);
  }

  @GetMapping("/consult")
  public ResponseEntity<List<RecipeVoteResponseDto>> findAllRecipes() {
    var recipeVote = recipeVoteService.consult();
    var resp = recipeVote.stream().map(recipe -> mapper.map(recipe, RecipeVoteResponseDto.class)).toList();
    return ResponseEntity.ok().body(resp);
  }

  @PutMapping("{id}")
  public ResponseEntity<RecipeVoteResponseDto> update(@RequestHeader("Authorization") String token,
      @PathVariable UUID id, @RequestBody @Valid RecipeVoteRequestDto request) {
    var recipe = mapper.map(request, RecipeVote.class);
    recipe.setId(id);
    recipe = recipeVoteService.update(token, recipe);
    var resp = mapper.map(recipe, RecipeVoteResponseDto.class);
    return ResponseEntity.ok().body(resp);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable(value = "id") UUID id) {
    recipeVoteService.delete(token, id);
    return ResponseEntity.noContent().build();
  }
}
