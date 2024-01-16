package com.fiap.posTech.parquimetro.service;

import com.fiap.posTech.parquimetro.controller.exception.ControllerNotFoundException;
import com.fiap.posTech.parquimetro.controller.exception.CustomException;
import com.fiap.posTech.parquimetro.model.*;
import com.fiap.posTech.parquimetro.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Service
@RequiredArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimistcLockingFaulureException(
            OptimisticLockingFailureException ex) {
        return  ResponseEntity.status(HttpStatus.CONFLICT).
                body("Erro de concorrência: O Veículo foi atualizado por outro usuario");
    }

    public Page<Veiculo> findAll(Pageable pageable) {
        return veiculoRepository.findAll(pageable);
    }
    @Transactional
    public Veiculo findById(String id) {
        try {
            return veiculoRepository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Veiculo não encontrado"));
        } catch (ControllerNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar Veiculo", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
        }
    }
    @Transactional
    public Veiculo save(Veiculo veiculo) {
        return veiculoRepository.save(veiculo);
    }
    @Transactional
    public Veiculo update(String id, Veiculo updatedVeiculo) {
        try {
            Veiculo veiculo = veiculoRepository.findById(id)
                    .orElseThrow(() -> new ControllerNotFoundException("Veículo não encontrado"));

            veiculo.setModelo(updatedVeiculo.getModelo());
            veiculo.setCor(updatedVeiculo.getCor());
            veiculo.setModelo(updatedVeiculo.getModelo());
            veiculo.setAnoFabrica(updatedVeiculo.getAnoFabrica());
            veiculo.setAnoModelo(updatedVeiculo.getAnoModelo());
            veiculo.setPlaca(updatedVeiculo.getPlaca());

            return veiculoRepository.save(veiculo);
        } catch (EntityNotFoundException e) {
            throw new ControllerNotFoundException("Veiculo com id:" + id + " não encontrado");
        }
    }
    @Transactional
    public void delete(String id) {
        veiculoRepository.deleteById(id);
    }

}
