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

import com.senai.labfoods.dtos.RecipeRequestDto;
import com.senai.labfoods.dtos.RecipeResponseDto;
import com.senai.labfoods.model.Recipe;
import com.senai.labfoods.service.RecipeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/recipe")
public class RecipeController {

  @Autowired
  private RecipeService recipeService;

  @Autowired
  private ModelMapper mapper;

  @PostMapping()
  public ResponseEntity<RecipeResponseDto> createRecipe(@RequestHeader("Authorization") String token,
      @RequestBody @Valid RecipeRequestDto request) {
    Recipe recipe = mapper.map(request, Recipe.class);
    recipeService.create(token, recipe);
    var resp = mapper.map(recipe, RecipeResponseDto.class);
    return ResponseEntity.created(URI.create(recipe.getId().toString())).body(resp);
  }

  // @GetMapping()
  // public ResponseEntity<List<RecipeResponseDto>>
  // findAllRecipes(@RequestHeader("Authorization") String token) {
  // var recipes = recipeService.consult(token);
  // var resp = recipes.stream().map(recipe -> mapper.map(recipe,
  // RecipeResponseDto.class)).toList();
  // return ResponseEntity.ok().body(resp);
  // }

  @GetMapping("{id}")
  public ResponseEntity<RecipeResponseDto> getRecipeId(@PathVariable UUID id) {
    var recipe = recipeService.getRecipeById(id);
    var resp = mapper.map(recipe, RecipeResponseDto.class);
    return ResponseEntity.ok().body(resp);
  }

  @GetMapping()
  public ResponseEntity<List<RecipeResponseDto>> findAllByUserId(@RequestHeader("Authorization") String token) {
    List<Recipe> recipes = recipeService.findAllByUserId(token);
    var resp = recipes.stream().map(recipe -> mapper.map(recipe, RecipeResponseDto.class)).toList();
    return ResponseEntity.ok().body(resp);
  }

  @PutMapping("{id}")
  public ResponseEntity<RecipeResponseDto> update(@RequestHeader("Authorization") String token, @PathVariable UUID id,
      @RequestBody @Valid RecipeRequestDto request) {
    var recipe = mapper.map(request, Recipe.class);
    recipe.setId(id);
    recipe = recipeService.update(token, recipe);
    var resp = mapper.map(recipe, RecipeResponseDto.class);
    return ResponseEntity.ok().body(resp);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable(value = "id") UUID id) {
    recipeService.delete(token, id);
    return ResponseEntity.noContent().build();
  }

}
