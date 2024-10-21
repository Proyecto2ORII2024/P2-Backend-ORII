package com.edu.unicauca.orii.core.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseException extends RuntimeException {

  private final int status;
  private final String messageDetail;

  protected BaseException(int status, String message) {
    super(message);
    this.status = status;
    this.messageDetail = message;
  }

}
