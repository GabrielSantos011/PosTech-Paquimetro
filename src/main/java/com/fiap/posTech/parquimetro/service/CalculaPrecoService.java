package com.fiap.posTech.parquimetro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;

@Service
public class CalculaPrecoService {

    private static final double PRECO_POR_HORA = 12.0;

//    @Autowired
//    private registroHorasRepository registroHorasRepository;
//
//    public double calcularPreco(RegistroDeHoras registro) throws ParseException {
//        long diferencaEmMilissegundos = registro.getHoraFinal().getTime() - registro.getHoraInicial().getTime();
//        double horas = diferencaEmMilissegundos / (60.0 * 60.0 * 1000.0);
//        double precoTotal = horas * PRECO_POR_HORA;
//
//        registroHoras registroHoras = new registroHoras();
//        registroHoras.setHoraInicial(registro.getHoraInicial());
//        registroHoras.setHoraFinal(registro.getHoraFinal());
//        registroHoras.setPreco(precoTotal);
//        registroHorasRepository.save(registroHoras);
//
//        return precoTotal;
//    }
}