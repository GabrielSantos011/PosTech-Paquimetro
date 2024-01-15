package com.fiap.posTech.parquimetro.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) throws Exception {
        this.javaMailSender = javaMailSender;
    }

    public void enviar(String enviarPara, String mensagem) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(enviarPara);
        email.setSubject("Tempo da vaga Expirando");
        email.setText(mensagem);
        javaMailSender.send(email);
    }
}
