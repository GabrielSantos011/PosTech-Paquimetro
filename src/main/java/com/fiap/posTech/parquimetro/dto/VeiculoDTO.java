package com.fiap.posTech.parquimetro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoDTO {
    String modelo;
    String cor;
    String anoFabrica;
    String anoModelo;
    String placa;
}

