package com.senai.labfoods.dtos;

import java.util.List;

import com.senai.labfoods.enums.RecipeType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponseDto {

  private String title;

  private String description;

  private String ingredients;

  private String preparationTime;

  private RecipeType recipeType;

  private String dietType;

  private String recipeOrigin;

  private List<RecipeVoteResponseDto> votes;
}
