package com.senai.labfoods.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.senai.labfoods.exception.AuthorizationFailedException;
import com.senai.labfoods.exception.InvalidRatingException;
import com.senai.labfoods.exception.RecordNotFoundException;
import com.senai.labfoods.model.Recipe;
import com.senai.labfoods.model.RecipeVote;
import com.senai.labfoods.model.User;
import com.senai.labfoods.repository.RecipeVoteRepository;

import jakarta.transaction.Transactional;

@Service
public class RecipeVoteService {

  @Autowired
  private RecipeVoteRepository recipeVoteRepository;

  @Autowired
  public UserService userService;

  @Autowired
  public RecipeService recipeService;

  public List<RecipeVote> consult() {
    return recipeVoteRepository.findAll();
  }

  @Transactional
  public RecipeVote create(String token, RecipeVote recipeVote) {
    User userLogado = userService.getUserByToken(token);

    Recipe recipe = recipeService.getRecipeById(recipeVote.getRecipe().getId());

    if (userLogado.getId().equals(recipe.getUser().getId())) {
      throw new AuthorizationFailedException("Não é possível votar em sua própria receita");
    }

    if (recipeVote.getRating() < 0 || recipeVote.getRating() > 5 || recipeVote.getRating() % 0.5 != 0) {
      throw new InvalidRatingException("Score must be between 0 and 5, in increments of 0.5.");
    }

    recipeVote.setId(null);
    recipeVote.setUser(userLogado);
    recipeVote.setRecipe(recipe);
    return recipeVoteRepository.save(recipeVote);
  }

  @Transactional
  public RecipeVote update(String token, RecipeVote updatedVote) {
    RecipeVote existingVote = recipeVoteRepository.findById(updatedVote.getId())
        .orElseThrow(() -> new IllegalArgumentException("Recipe vote not found"));

    User userLogado = userService.getUserByToken(token);

    if (!existingVote.getUser().getId().equals(userLogado.getId())) {
      throw new AuthorizationFailedException("Você não tem permissão para atualizar este voto");
    }

    if (updatedVote.getRating() < 0 || updatedVote.getRating() > 5 || updatedVote.getRating() % 0.5 != 0) {
      throw new IllegalArgumentException("O voto deve estar entre 0 e 5, em incrementos de 0,5. ");
    }

    existingVote.setRating(updatedVote.getRating());
    existingVote.setFeedback(updatedVote.getFeedback());

    return recipeVoteRepository.save(existingVote);
  }

  @Transactional
  public void delete(String token, UUID id) {
    RecipeVote existingVote = recipeVoteRepository.findById(id)
        .orElseThrow(() -> new RecordNotFoundException("Recipe vote não encontrada"));

    User userLogado = userService.getUserByToken(token);

    Recipe recipe = existingVote.getRecipe();

    if (!existingVote.getUser().getId().equals(userLogado.getId())
        && (!recipe.getUser().getId().equals(userLogado.getId()))) {
      throw new AuthorizationFailedException("Você não tem permissão para deletar este voto");
    }

    recipeVoteRepository.delete(existingVote);
  }
}
