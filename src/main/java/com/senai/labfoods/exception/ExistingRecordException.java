package com.senai.labfoods.exception;

public class ExistingRecordException extends RuntimeException {
  public ExistingRecordException(String mensagem) {
    super(mensagem);
  }
}
