package com.senai.labfoods.exception;

public class AccessDeniedException extends RuntimeException {
  public AccessDeniedException(String msg) {
    super(msg);
  }
}