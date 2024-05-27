package com.senai.labfoods.dtos;

import java.util.List;

import com.senai.labfoods.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
  private String name;
  private Gender gender;
  private String email;
  private List<RecipeResponseDto> recipes;
}
