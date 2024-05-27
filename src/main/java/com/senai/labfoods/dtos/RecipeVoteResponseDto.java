package com.senai.labfoods.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeVoteResponseDto {

  private double rating;

  private String feedback;

}
