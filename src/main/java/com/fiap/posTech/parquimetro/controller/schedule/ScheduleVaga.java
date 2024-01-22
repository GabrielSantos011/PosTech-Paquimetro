package com.fiap.posTech.parquimetro.controller.schedule;

import com.fiap.posTech.parquimetro.model.Park;
import com.fiap.posTech.parquimetro.repository.ParkRepository;
import com.fiap.posTech.parquimetro.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
public class ScheduleVaga {
    @Autowired
    private EmailService emailService;
    @Autowired
    private ParkRepository repository;

    private final Logger logger = LoggerFactory.getLogger(ScheduleVaga.class);
    private final long Minuto = 120000; // 2 minutos

    @Scheduled(fixedDelay = Minuto)
    public void validaVaga(){
        try {
            logger.info("Verificando o tempo de vaga para enviar email");
            List<Park> lista = repository.obterListaEnviarEmail((double) 0);
            lista.forEach(
                    park -> {
                        LocalDateTime now = LocalDateTime.now();
                        Duration drt = Duration.between(park.getSaida(), now);
                        LocalTime lt = LocalTime.ofNanoOfDay(drt.toNanos());
                        if (lt.getMinute() >= 50) {
                            var tempo = lt.format(DateTimeFormatter.ofPattern("HH:mm"));
                            enviaEmail(tempo, park);
                        }
                    }
            );
        } catch (Exception e) {
            logger.error("Erro ao executar a validação de vagas. Erro: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    private void enviaEmail(String tempo, Park park) {
        var email = park.getPessoa().getEmail();
        if (email != null || !email.isEmpty()) {
            logger.info("Enviando email");
            String enviarPara = email;
            String mensagem = "O tempo de sua vaga está expirando....\n Fique atento para renovar o tempo da vaga. \n Tempo total na vaga: " + tempo;
            emailService.enviar(enviarPara, mensagem, "Tempo da vaga Expirando");
            logger.info("Email enviado");
        }
    }

}
