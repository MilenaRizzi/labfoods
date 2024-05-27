package com.senai.labfoods.dtos;

import java.util.List;
import java.util.UUID;

import com.senai.labfoods.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryUserDto {
  private UUID id;
  private String email;
  private List<Role> roles;
}
