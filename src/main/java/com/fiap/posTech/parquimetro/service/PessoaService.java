package com.fiap.posTech.parquimetro.service;

import com.fiap.posTech.parquimetro.controller.exception.ControllerNotFoundException;
import com.fiap.posTech.parquimetro.controller.exception.CustomException;
import com.fiap.posTech.parquimetro.controller.exception.ParkingException;
import com.fiap.posTech.parquimetro.model.*;
import com.fiap.posTech.parquimetro.repository.EnderecoRepository;
import com.fiap.posTech.parquimetro.repository.PessoaRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;
    private final VeiculoRepository veiculoRepository;

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimistcLockingFaulureException(
            OptimisticLockingFailureException ex) {
        return  ResponseEntity.status(HttpStatus.CONFLICT).
                body("Erro de concorrência: A Pessoa foi atualizado por outro usuario");
    }

    public Page<Pessoa> findAll(Pageable pageable) {
        return pessoaRepository.findAll(pageable);
    }
    @Transactional
    public Pessoa findById(String id) {
        try {
            return pessoaRepository.findById(id).orElseThrow(() -> new ControllerNotFoundException("Usuário não encontrado"));
        } catch (ControllerNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Erro ao buscar usuário", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
        }
    }
    @Transactional
    public Pessoa save(Pessoa pessoa) {
        if (pessoa.getEndereco() != null) {
            enderecoRepository.save(pessoa.getEndereco());
        }

        List<Veiculo> veiculos = pessoa.getVeiculos();
        if (veiculos != null && !veiculos.isEmpty()) {
            veiculos.forEach(veiculoRepository::save);
        }

        return pessoaRepository.save(pessoa);
    }
    @Transactional
    public Pessoa update(String id, Pessoa updatedPessoa) {
        try {
            Pessoa pessoa = pessoaRepository.findById(id)
                    .orElseThrow(() -> new ControllerNotFoundException("Usuário não encontrado"));

            pessoa.setNome(updatedPessoa.getNome());
            pessoa.setEmail(updatedPessoa.getEmail());
            pessoa.setCpf(updatedPessoa.getCpf());
            pessoa.setRg(updatedPessoa.getRg());
            pessoa.setCelular(updatedPessoa.getCelular());
            pessoa.setDataNascimento(updatedPessoa.getDataNascimento());

            if (updatedPessoa.getEndereco() != null) {
                updateEndereco(pessoa.getEndereco(), updatedPessoa.getEndereco());
            }

            List<Veiculo> veiculos = updatedPessoa.getVeiculos();
            if (veiculos != null && !veiculos.isEmpty()) {
                updateVeiculos(pessoa, veiculos);
            }

            return pessoaRepository.save(pessoa);
        } catch (EntityNotFoundException e) {
            throw new ControllerNotFoundException("Usuário com id:" + id + " não encontrado");
        }
    }
    @Transactional
    public void delete(String id) {
        pessoaRepository.deleteById(id);
    }

    @Transactional
    public Pessoa pagarEstacionamento(String pessoaId, double valor) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ControllerNotFoundException("Usuário não encontrado"));

        EnumPagamento formaPagamento = pessoa.getFormaPagamento();

        if (formaPagamento == null) {
            throw new ParkingException("Forma de Pagamento não registrada para o usuário");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setValor(valor);
        pagamento.setTipoPagamento(formaPagamento);
        pagamento.setPessoa(pessoa);

        pessoa.adicionarPagamento(pagamento);

        pessoa = pessoaRepository.save(pessoa);

        return pessoa;
    }

    @Transactional
    public Pessoa definirFormaPagamento(String pessoaId, EnumPagamento formaPagamento) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ControllerNotFoundException("Usuário não encontrado"));

        pessoa.definirFormaPagamento(formaPagamento);

        pessoa = pessoaRepository.save(pessoa);

        return pessoa;
    }

    private void updateEndereco(Endereco endereco, Endereco updatedEndereco) {
        endereco.setTipoLogradouro(updatedEndereco.getTipoLogradouro());
        endereco.setLogradouro(updatedEndereco.getLogradouro());
        endereco.setNumero(updatedEndereco.getNumero());
        endereco.setComplemento(updatedEndereco.getComplemento());
        endereco.setBairro(updatedEndereco.getBairro());
        endereco.setCidade(updatedEndereco.getCidade());
        endereco.setUf(updatedEndereco.getUf());
        endereco.setCep(updatedEndereco.getCep());

        enderecoRepository.save(endereco);
    }

    private void updateVeiculos(Pessoa pessoa, List<Veiculo> updatedVeiculos) {
        List<Veiculo> veiculos = new ArrayList<>();

        for (Veiculo updatedVeiculo : updatedVeiculos) {
            Veiculo veiculo = pessoa.getVeiculos()
                    .stream()
                    .filter(v -> v.getId().equals(updatedVeiculo.getId()))
                    .findFirst()
                    .orElse(null);

            if (veiculo != null) {
                veiculo.setModelo(updatedVeiculo.getModelo());
                veiculo.setCor(updatedVeiculo.getCor());
                veiculo.setAnoFabrica(updatedVeiculo.getAnoFabrica());
                veiculo.setAnoModelo(updatedVeiculo.getAnoModelo());
                veiculo.setPlaca(updatedVeiculo.getPlaca());
                veiculoRepository.save(veiculo);
            } else {
                Veiculo novoVeiculo = new Veiculo();
                novoVeiculo.setModelo(updatedVeiculo.getModelo());
                novoVeiculo.setCor(updatedVeiculo.getCor());
                novoVeiculo.setAnoFabrica(updatedVeiculo.getAnoFabrica());
                novoVeiculo.setAnoModelo(updatedVeiculo.getAnoModelo());
                novoVeiculo.setPlaca(updatedVeiculo.getPlaca());
                veiculoRepository.save(novoVeiculo);
                veiculos.add(novoVeiculo);
            }
        }

        pessoa.setVeiculos(veiculos);
    }
}
