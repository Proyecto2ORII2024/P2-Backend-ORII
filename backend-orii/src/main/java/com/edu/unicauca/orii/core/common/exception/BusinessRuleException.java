package com.edu.unicauca.orii.core.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessRuleException extends BaseException {

  public BusinessRuleException(int status, String message) {
    super(status, message);
  }
}
