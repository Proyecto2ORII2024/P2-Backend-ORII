package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.exception.handler;

import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.exception.BusinessRuleException;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.exception.messages.MessageLoader;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.exception.messages.MessagesConstant;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.exception.common.ResponseDto;

import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global Exception Handler to manage various exception types.
 * @author JulianRuano
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles exceptions.
   * Logs the error message and returns a response for this specific exception.
   *
   * @param e The Exception instance.
   * @return Response entity containing error details.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult().getAllErrors().forEach((error) -> {
          String fieldName = ((FieldError) error).getField();
          String errorMessage = error.getDefaultMessage();
          errors.put(fieldName, errorMessage);
      });
      return ResponseEntity.badRequest().body(errors);
  }
  


  /**
   * Handles exceptions.
   * Logs the error message and returns a response for this specific exception.
   *
   * @param e The BusinessRuleException instance.
   * @return Response entity containing error details.
   */
  @ExceptionHandler(BusinessRuleException.class)
  public ResponseEntity<ResponseDto<Object>> handleBusinessRuleException(BusinessRuleException e) {
    return new ResponseDto<>(e.getStatus(), e.getMessage()).of();
  }

  /**
   * Handles MissingServletRequestParameterException.
   * Logs the error and returns a response entity with error details.
   *
   * @param ex The MissingServletRequestParameterException instance.
   * @return Response entity containing error details.
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseDto<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
    return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(),
        MessageLoader.getInstance().getMessage(MessagesConstant.EM003, ex.getParameterName()));
  }


  /**
   * Handles RuntimeException.
   * Logs the error and returns a response entity with error details.
   *
   * @param ex The RuntimeException instance.
   * @return Response entity containing error details.
   */
  @ExceptionHandler(RuntimeException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseDto<Void> handleRuntimeException(RuntimeException ex) {
    return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        MessageLoader.getInstance().getMessage(MessagesConstant.EM001));
  }


  /**
   * Handles `MethodArgumentTypeMismatchException` by returning a response with a 400 BAD REQUEST status.
   *
   * @param ex the exception thrown when a method argument type mismatch occurs.
   * @return a response with the error details.
   */

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseDto<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(),
        MessageLoader.getInstance().getMessage(MessagesConstant.EM011, ex.getPropertyName(),
            Objects.requireNonNull(ex.getRequiredType()).getSimpleName()));
  }

}
