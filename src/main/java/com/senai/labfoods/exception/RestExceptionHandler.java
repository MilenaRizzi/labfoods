package com.senai.labfoods.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ExistingRecordException.class)
    public ResponseEntity<Object> handleRegistroExistenteException(ExistingRecordException e) {
        Map<String, String> retorno = new HashMap<>();
        retorno.put("erro", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(retorno);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<Object> handleRegistroNaoEncontradoException(RecordNotFoundException e) {
        Map<String, String> retorno = new HashMap<>();
        retorno.put("erro", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(retorno);
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<Object> handleAuthenticationFailedException(AuthorizationFailedException e) {
        Map<String, String> retorno = new HashMap<>();
        retorno.put("erro", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(retorno);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAuthenticationFailedException(AccessDeniedException e) {
        Map<String, String> retorno = new HashMap<>();
        retorno.put("erro", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(retorno);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fieldErrors);
    }

}