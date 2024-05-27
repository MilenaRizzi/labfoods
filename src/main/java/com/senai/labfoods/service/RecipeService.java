package com.senai.labfoods.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senai.labfoods.exception.AuthorizationFailedException;
import com.senai.labfoods.exception.RecordNotFoundException;
import com.senai.labfoods.model.Recipe;
import com.senai.labfoods.model.RecipeVote;
import com.senai.labfoods.model.User;
import com.senai.labfoods.repository.RecipeRepository;
import com.senai.labfoods.repository.RecipeVoteRepository;

import jakarta.transaction.Transactional;

@Service
public class RecipeService {
  @Autowired
  private RecipeRepository recipeRepository;

  @Autowired
  private RecipeVoteRepository recipeVoteRepository;

  @Autowired
  private UserService userService;

  public Recipe getRecipeById(UUID id) {
    return recipeRepository.findById(id)
        .orElseThrow(() -> new RecordNotFoundException("Receita não encontrada com o código informado"));
  }

  public List<Recipe> findAllByUserId(String token) {
    User user = userService.getUserByToken(token);
    return recipeRepository.findByUser_Id(user.getId());
  }

  public long countRecipes() {
    return recipeRepository.count();
  }

  @Transactional
  public Recipe create(String token, Recipe recipe) {
    User user = userService.getUserByToken(token);
    recipe.setId(null);
    recipe.setUser(user);
    return recipeRepository.save(recipe);
  }

  @Transactional
  public Recipe update(String token, Recipe recipe) {
    User user = userService.getUserByToken(token);
    Recipe existingRecipe = getRecipeById(recipe.getId());

    if (!user.getId().equals(existingRecipe.getUser().getId())) {
      throw new AuthorizationFailedException("Não é permitido editar a receita de outro usuário");
    }

    existingRecipe.setTitle(recipe.getTitle());
    existingRecipe.setDescription(recipe.getDescription());
    existingRecipe.setIngredients(recipe.getIngredients());
    existingRecipe.setPreparationTime(recipe.getPreparationTime());
    existingRecipe.setPreparationMethod(recipe.getPreparationMethod());
    existingRecipe.setRecipeType(recipe.getRecipeType());
    existingRecipe.setDietType(recipe.getDietType());
    existingRecipe.setRecipeOrigin(recipe.getRecipeOrigin());

    return recipeRepository.save(existingRecipe);
  }

  public void delete(String token, UUID id) {
    Recipe recipe = getRecipeById(id);
    User user = userService.getUserByToken(token);

    if (!user.getId().equals(recipe.getUser().getId())) {
      throw new AuthorizationFailedException("Não é permitido excluir a receita de outro usuário");
    }

    recipeRepository.deleteById(id);
  }

  public List<Recipe> getTopVotedRecipes(int limit) {
    List<RecipeVote> allRecipeVotes = recipeVoteRepository.findAll();

    Map<Recipe, List<RecipeVote>> votesByRecipe = allRecipeVotes.stream()
        .collect(Collectors.groupingBy(RecipeVote::getRecipe));
    Map<Recipe, Double> weightedRatingsByRecipe = new HashMap<>();

    for (Map.Entry<Recipe, List<RecipeVote>> entry : votesByRecipe.entrySet()) {
      Recipe recipe = entry.getKey();
      List<RecipeVote> votes = entry.getValue();
      double averageRating = votes.stream().mapToDouble(RecipeVote::getRating).average().orElse(0.0);
      int voteCount = votes.size();
      double weightedRating = averageRating * voteCount;
      weightedRatingsByRecipe.put(recipe, weightedRating);
    }

    List<Recipe> topVotedRecipes = weightedRatingsByRecipe.entrySet().stream()
        .sorted(Map.Entry.<Recipe, Double>comparingByValue().reversed())
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());

    if (topVotedRecipes.size() > limit) {
      topVotedRecipes = topVotedRecipes.subList(0, limit);
    }

    return topVotedRecipes;
  }
}
