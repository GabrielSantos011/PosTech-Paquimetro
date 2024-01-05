package com.fiap.posTech.parquimetro.exception;

public class TrataMensagem {
    public String TrataMensagemErro(String message) {
        String smensagem = "\"" + message + "\"";
        return  "{ \"message\": " + smensagem + " }";
    }
}
