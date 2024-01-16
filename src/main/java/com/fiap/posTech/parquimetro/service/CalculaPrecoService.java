package com.fiap.posTech.parquimetro.service;

import com.fiap.posTech.parquimetro.model.Park;

import com.fiap.posTech.parquimetro.repository.ParkRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class CalculaPrecoService {

    private static final double PRECO_POR_HORA = 12.0;
    public static double getPrecoPorHora() {
        return PRECO_POR_HORA;
    }

    @Autowired
    private ParkRepository parkRepository;

    @Transactional
    public double calcularPreco(Park registro) {
        try {
            LocalDateTime entradaLocalDateTime = registro.getEntrada();
            LocalDateTime saidaLocalDateTime = registro.getSaida();

            if (entradaLocalDateTime == null || saidaLocalDateTime == null) {
                throw new IllegalArgumentException("Datas de entrada ou saída nulas.");
            }

            long diferencaEmMilissegundos = Duration.between(entradaLocalDateTime, saidaLocalDateTime).toMillis();
            double horas = diferencaEmMilissegundos / (60.0 * 60.0 * 1000.0);
            double valorHora = getPrecoPorHora();
            double valorCobrado = horas * getPrecoPorHora();

            valorCobrado = arredondar(valorCobrado);

            registro.setValorHora(valorHora);
            registro.setValorCobrado(valorCobrado);

            return valorCobrado;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao calcular o preço.", e);
        }
    }
    private static double arredondar(double valorCobrado) {
        return Math.round(valorCobrado * 100.0)/100.0;
    }
}

