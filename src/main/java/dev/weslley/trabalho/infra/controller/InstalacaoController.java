package dev.weslley.trabalho.infra.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.weslley.trabalho.doamin.Cliente;
import dev.weslley.trabalho.doamin.Instalacao;
import dev.weslley.trabalho.infra.controller.exceptions.RecursoNotFoundException;
import dev.weslley.trabalho.infra.repository.ClienteRepository;
import dev.weslley.trabalho.infra.repository.InstalacaoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Acme AP Instalação Service")
public class InstalacaoController {

	@Autowired
	private InstalacaoRepository instalacaoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@ApiOperation(value = "Mostra a lista de instalações")
	// Controle de versão explicito na URI
	@GetMapping("v1/instalacoes")
	public List<Instalacao> getAllInstalacoes() {
		return instalacaoRepository.findAll();
	}

	@ApiOperation(value = "Consulta uma instalação pelo código")
	@GetMapping("v1/instalacoes/{codigo}")
	public Instalacao getInstalacao(@PathVariable String codigo) {

		return instalacaoRepository.findByCodigo(codigo)
				.orElseThrow(() -> new RecursoNotFoundException("codigo instalacao - " + codigo));
	}

	@ApiOperation(value = "Consulta uma instalação pelo CPF")
	@GetMapping("v1/instalacoes/cpf/{cpf}")
	public List<Instalacao> getInstalacaoPorCPF(@PathVariable String cpf) {

		Cliente cliente;

		cliente = clienteRepository.findByCpf(cpf).orElseThrow(() -> new RecursoNotFoundException("CPF - " + cpf));

		List<Instalacao> listaInstalacao = instalacaoRepository.findByCliente(cliente);

		return listaInstalacao;
	}

	@ApiOperation(value = "Cadastrar uma nova instalação")
	@PostMapping("v1/instalacoes")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> cadastrarInstalacao(@RequestBody Instalacao instalacao) {
		Optional<Cliente> cliente;
		Instalacao instalacaoCriada;
		URI location = null;
		try {
			cliente = clienteRepository.findByCpf(instalacao.getCliente().getCpf());

			if (cliente.get() == null)
				throw new RecursoNotFoundException("CPF - " + instalacao.getCliente().getCpf());

			instalacao.setCliente(cliente.get());

			instalacaoCriada = instalacaoRepository.save(instalacao);
			location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(instalacaoCriada.getId()).toUri();
		} catch (Exception e) {
			// TODO: handle exception
			throw new RecursoNotFoundException("CPF - " + instalacao.getCliente().getCpf());
		}

		return ResponseEntity.created(location).build();
	}

}