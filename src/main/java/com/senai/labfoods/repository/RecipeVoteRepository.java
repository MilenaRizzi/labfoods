package com.senai.labfoods.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.senai.labfoods.model.RecipeVote;

@Repository
public interface RecipeVoteRepository extends JpaRepository<RecipeVote, UUID> {

}
