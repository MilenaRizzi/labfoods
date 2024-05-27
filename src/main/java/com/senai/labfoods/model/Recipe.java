package com.senai.labfoods.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.senai.labfoods.enums.RecipeType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_RECIPE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false, length = 255)
  private String title;

  @Column(nullable = false, length = 1000)
  private String description;

  @Column(nullable = false, length = 1000)
  private String ingredients;

  @Column(nullable = false)
  private String preparationTime;

  @Column(nullable = false, length = 2000)
  private String preparationMethod;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RecipeType recipeType;

  @Column(nullable = false)
  private String dietType;

  @Column(nullable = false)
  private String recipeOrigin;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private User user;

  @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private List<RecipeVote> votes = new ArrayList<>();
}
