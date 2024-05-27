package com.senai.labfoods.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Valid
public class Address {

  @NotEmpty(message = "Rua é obrigatoria")
  @Size(min = 3, max = 50, message = "O nome da rua deve ter entre 3 e 50 caracteres")
  private String street;

  @NotNull(message = "Número é obrigatorio")
  private int number_home;

  @Size(min = 3, max = 50, message = "O complemento deve ter entre 3 e 50 caracteres")
  private String complement;

  @NotEmpty(message = "Bairro é obrigatorio")
  @Size(min = 3, max = 50, message = "O nome do bairro deve ter entre 3 e 50 caracteres")
  private String neighborhood;

  @NotEmpty(message = "Cidade é obrigatoria")
  @Size(min = 2, max = 30, message = "O nome da cidade deve ter entre 2 e 30 caracteres")
  private String city;

  @NotEmpty(message = "Estado é obrigatorio")
  @Size(min = 2, max = 30, message = "O nome do estado deve ter entre 2 e 30 caracteres")
  private String state;

  @NotEmpty(message = "CEP é obrigatório")
  @Size(min = 8, max = 9, message = "O CEP deve ter entre 8 e 9 caracteres")
  private String CEP;
}
