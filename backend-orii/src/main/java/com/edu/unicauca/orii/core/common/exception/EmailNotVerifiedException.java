package com.edu.unicauca.orii.core.common.exception;

public class EmailNotVerifiedException extends  BaseException{
    public EmailNotVerifiedException(int status,String message) {
        super(status,message);
    }
}
