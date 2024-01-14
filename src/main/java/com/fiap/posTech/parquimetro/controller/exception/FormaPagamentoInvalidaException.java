package com.fiap.posTech.parquimetro.controller.exception;

import org.springframework.http.HttpStatus;

public class FormaPagamentoInvalidaException extends CustomException{
    public FormaPagamentoInvalidaException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value(), "Bad Request");
    }
}
