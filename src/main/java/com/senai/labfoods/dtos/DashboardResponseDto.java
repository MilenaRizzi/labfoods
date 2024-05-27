package com.senai.labfoods.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardResponseDto {
    private long totalUsers;
    private long totalRecipes;
    private List<RecipeResponseDto> topVotedRecipes;
    private List<UserResponseDto> users;

}