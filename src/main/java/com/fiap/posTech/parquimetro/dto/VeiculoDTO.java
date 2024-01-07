package com.fiap.posTech.parquimetro.dto;

import java.time.Year;

public record VeiculoDTO(
        String modelo,
        String cor,
        Year anoFabrica,
        Year anoModelo,
        String placa
) {
}
