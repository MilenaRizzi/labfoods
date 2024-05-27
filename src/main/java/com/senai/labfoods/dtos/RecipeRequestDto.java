package com.senai.labfoods.dtos;

import com.senai.labfoods.enums.RecipeType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestDto {

  @NotNull(message = "Campo obrigatório")
  private String title;

  @NotNull(message = "Campo obrigatório")
  private String description;

  @NotNull(message = "Campo obrigatório")
  private String ingredients;

  @NotNull(message = "Campo obrigatório")
  private String preparationTime;

  @NotNull(message = "Campo obrigatório")
  private String preparationMethod;

  @NotNull(message = "Campo obrigatório")
  private RecipeType recipeType;

  @NotNull(message = "Campo obrigatório")
  private String dietType;

  @NotNull(message = "Campo obrigatório")
  private String recipeOrigin;
}
