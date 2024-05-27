package com.senai.labfoods.exception;

public class RecordNotFoundException extends RuntimeException {
  public RecordNotFoundException(String msg) {
    super(msg);
  }
}
