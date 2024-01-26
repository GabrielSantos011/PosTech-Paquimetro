package com.fiap.posTech.parquimetro.service;

import com.fiap.posTech.parquimetro.controller.exception.ControllerNotFoundException;
import com.fiap.posTech.parquimetro.controller.exception.CustomException;
import com.fiap.posTech.parquimetro.controller.exception.ErrorResponse;
import com.fiap.posTech.parquimetro.model.Park;
import com.fiap.posTech.parquimetro.model.Pessoa;
import com.fiap.posTech.parquimetro.model.Veiculo;
import com.fiap.posTech.parquimetro.repository.ParkRepository;
import com.fiap.posTech.parquimetro.repository.PessoaRepository;
import com.fiap.posTech.parquimetro.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ParkService {

    private static final Logger logger = LoggerFactory.getLogger(ParkService.class);

    private final ReciboService reciboService;
    private final PessoaService pessoaService;
    private final ParkRepository parkRepository;
    private final PessoaRepository pessoaRepository;
    private final VeiculoRepository veiculoRepository;

    @Transactional
    public ResponseEntity<?> checkin(Park park) {
        try {
            Pessoa pessoa = pessoaRepository.findById(park.getPessoa().getId())
                    .orElseThrow(() -> new ControllerNotFoundException("Pessoa não encontrada"));
            Veiculo veiculo = veiculoRepository.findById(park.getVeiculo().getId())
                    .orElseThrow(() -> new ControllerNotFoundException("Veículo não encontrado"));

            if (!pessoa.getVeiculos().stream().anyMatch(v -> v.getId().equals(veiculo.getId()))) {
                logger.error("O veículo não está vinculado à pessoa.");
                throw new CustomException("O veículo não está vinculado à pessoa.", HttpStatus.BAD_REQUEST.value(), "Bad Request");
            }

            park.setPessoa(pessoa);
            LocalDateTime now = LocalDateTime.now();
            park.setEntrada(now);
            double valorHora = CalculaPrecoService.getPrecoPorHora();
            park.setValorHora(valorHora);

            // Salva o registro no banco de dados
            park.setAtiva(true);
            parkRepository.save(park);
            pessoa.adicionarPark(park);
            pessoaService.save(pessoa);

            return new ResponseEntity<>("Parking cadastrado com sucesso. Valor da hora: R$ " + valorHora,
                    HttpStatus.CREATED);

        } catch (ControllerNotFoundException e) {
            throw e;
        } catch (CustomException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getTimestamp(), e.getStatus(), e.getError(), e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(errorResponse);
        } catch (Exception e) {
            throw new CustomException("Erro ao cadastrar park", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
        }
    }

    @Transactional
    public ResponseEntity<?> checkout(String id) {
        try {
            Park park = parkRepository.findById(id)
                    .orElseThrow(() -> new ControllerNotFoundException("Parking não encontrado"));
            LocalDateTime now = LocalDateTime.now();
            Duration drt = Duration.between(park.getEntrada(), now);
            if(park.getAtiva()) {
                LocalTime lt = LocalTime.ofNanoOfDay(drt.toNanos());
                var tempo = lt.format(DateTimeFormatter.ofPattern("HH:mm"));
                park.setSaida(now);
                park.setPermanencia(tempo);
                park.setAtiva(false);
            } else {
                throw new CustomException("O parking já foi finalizado!", HttpStatus.BAD_REQUEST.value(), "Bad Request");
            }
            // Chama o serviço de cálculo de preço
            CalculaPrecoService calculaPrecoService = new CalculaPrecoService();
            double valorCobrado = calculaPrecoService.calcularPreco(park);

            parkRepository.save(park);
            reciboService.emitirRecibo(park);

            return new ResponseEntity<>("Checkout realizado com sucesso. Valor total: R$ " + valorCobrado,
                    HttpStatus.OK);

        } catch (ControllerNotFoundException e) {
            throw new CustomException("Parking não encontrado", HttpStatus.NOT_FOUND.value(), "Not Found");
        } catch (CustomException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getTimestamp(), e.getStatus(), e.getError(), e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(errorResponse);
        } catch (Exception e) {
            throw new CustomException("Erro ao alterar o Parking", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
        }
    }

    @Transactional(readOnly = true)
    public Page<Park> listaTodos(Pageable page) {
        Sort sort = Sort.by("entrada").descending();
        Pageable lista = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
        return this.parkRepository.findAll(lista);
    }

    public Optional<Park> findById(String id) {
        return this.parkRepository.findById(id);
    }

    public Page<Park> getParksAtivos(Pageable page) {
        Sort sort = Sort.by("entrada").descending();
        Pageable lista = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
        return this.parkRepository.findAllByAtivaIsTrue(lista);
    }

    public List<Park> getParksAtivosComTempoFixo(){
        return this.parkRepository.findAllByAtivaIsTrueAndTipoTempoFIXO();
    }

}
