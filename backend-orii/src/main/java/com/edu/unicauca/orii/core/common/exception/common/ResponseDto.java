package com.edu.unicauca.orii.core.common.exception.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

/**
 * Dto to manage the response of the API.
 *
 * @author JulianRuano
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * Response DTO class.
 *
 * @param <T> the type of the response object
 */
public class ResponseDto<T> {
  private T data;
  private Integer status;
  private String message;
  private String errorCode;

  public ResponseDto(int status, String message) {
    this.status = status;
    this.message = message;
  }


  public ResponseDto(int status, String message, T data) {
    this.status = status;
    this.message = message;
    this.data = data;
  }


  public ResponseDto(int status, String message, String errorCode) {
    this.status = status;
    this.message = message;
    this.errorCode = errorCode;
  }

  public ResponseEntity<ResponseDto<T>> of() {
    return ResponseEntity.status(this.status).body(this);
  }
}
