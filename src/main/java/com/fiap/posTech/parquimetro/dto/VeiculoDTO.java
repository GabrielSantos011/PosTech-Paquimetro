package com.fiap.posTech.parquimetro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoDTO {
    String id;
    String modelo;
    String cor;
    String anoFabrica;
    String anoModelo;
    String placa;
}

