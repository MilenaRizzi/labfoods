package com.senai.labfoods.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senai.labfoods.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

  List<Recipe> findByUser_Id(UUID id);

  long countByUserId(UUID userId);
}
