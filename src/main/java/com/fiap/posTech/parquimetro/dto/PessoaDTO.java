package com.fiap.posTech.parquimetro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record PessoaDTO(

        String codigo,
        @NotBlank(message = "O nome não pode estar em branco.")
        String nome,
        @Email(message = "E-mail inválido.")
        String email,
        @CPF(message = "CPF inválido.")
        String cpf,
        String rg,
        String celular,
        LocalDate dataNascimento,
        EnderecoDTO enderecoDTO,
        VeiculoDTO veiculoDTO
) {
}
