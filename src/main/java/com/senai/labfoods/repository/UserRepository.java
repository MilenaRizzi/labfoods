package com.senai.labfoods.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.senai.labfoods.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  boolean existsByEmail(String email);

  boolean existsByCpf(String cpf);

  Optional<User> findByEmail(String email);

  @Query("SELECT user FROM User user JOIN FETCH user.roles WHERE user.email = :email")
  User findByUsernameFetchRoles(@Param("email") String email);

}
