package com.senai.labfoods.exception;

public class AuthorizationFailedException extends RuntimeException {
  public AuthorizationFailedException(String msg) {
    super(msg);
  }
}