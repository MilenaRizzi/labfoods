package com.senai.labfoods.dtos;

import java.time.LocalDate;

import com.senai.labfoods.enums.Gender;
import com.senai.labfoods.model.Address;

import jakarta.persistence.Embedded;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateRequestDto {
  @NotNull(message = "Campo obrigatório")
  private String name;

  @NotNull(message = "Campo obrigatório")
  private Gender gender;

  @NotNull(message = "Campo obrigatório")
  @Embedded
  @Valid
  private Address address;

  @NotNull(message = "Campo obrigatório")
  @Email(message = "Email inválido")
  private String email;

  @NotNull(message = "Campo obrigatório")
  private String password;

  @NotNull(message = "Campo obrigatório")
  private LocalDate dateOfBirth;
}
