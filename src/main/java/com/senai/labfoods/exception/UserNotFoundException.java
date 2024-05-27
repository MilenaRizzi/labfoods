package com.senai.labfoods.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super("Usuário não encontrado.");
  }

}
