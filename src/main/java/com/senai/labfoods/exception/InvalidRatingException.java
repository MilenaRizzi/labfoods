package com.senai.labfoods.exception;

public class InvalidRatingException extends RuntimeException {
  public InvalidRatingException(String msg) {
    super(msg);
  }
}