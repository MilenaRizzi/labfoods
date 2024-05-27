package com.senai.labfoods.dtos;

import com.senai.labfoods.model.Recipe;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeVoteRequestDto {

  @NotNull(message = "Campo obrigatório")
  private Recipe recipe;

  @NotNull(message = "Campo obrigatório")
  private double rating;

  @NotNull(message = "Campo obrigatório")
  private String feedback;
}
