package com.edu.unicauca.orii.core.common.exception.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.edu.unicauca.orii.core.common.exception.BusinessRuleException;
import com.edu.unicauca.orii.core.common.exception.EmailNotVerifiedException;
import com.edu.unicauca.orii.core.common.exception.common.ResponseDto;
import com.edu.unicauca.orii.core.common.exception.messages.MessageLoader;
import com.edu.unicauca.orii.core.common.exception.messages.MessagesConstant;

/**
 * Global Exception Handler to manage various exception types.
 * @author JulianRuano
 * @author JhossefCons
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles exceptions.
   * Logs the error message and returns a response for this specific exception.
   *
   * @param ex The Exception instance.
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
   * @param e The HttpRequestMethodNotSupportedException instance.
   * @return Response entity containing error details.
   */
   @ExceptionHandler(NoResourceFoundException.class)
   public ResponseEntity<ResponseDto<Object>> handleNoResourceFoundException(NoResourceFoundException e) {
     return new ResponseDto<>(405, e.getMessage()).of();
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
   * Handles exceptions.
   * Logs the error message and returns a response for this specific exception.
   *
   * @param e The EmailNotVerifiedException instance.
   * @return Response entity containing error details.
   */
  @ExceptionHandler(EmailNotVerifiedException.class)
  public ResponseEntity<ResponseDto<Object>> handleEmailNotVerifiedException(EmailNotVerifiedException e){
    return new ResponseDto<>(e.getStatus(), e.getMessage()).of();
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ResponseDto<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
   
    if(e.getMessage().contains("Cannot deserialize value of type `java.util.Date` from String")) {
      return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), MessageLoader.getInstance().getMessage(MessagesConstant.EM012)).of();
    }else {
      return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), MessageLoader.getInstance().getMessage(MessagesConstant.EM009)).of();
    } 
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


  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ResponseDto<Object>> handleAccessDeniedException(AccessDeniedException e) {
    return new ResponseDto<>(HttpStatus.FORBIDDEN.value(), e.getMessage()).of();
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ResponseDto<Object>> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
    return new ResponseDto<>(HttpStatus.FORBIDDEN.value(), e.getMessage()).of();
  }

}
