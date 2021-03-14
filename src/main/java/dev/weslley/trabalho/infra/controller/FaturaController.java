package dev.weslley.trabalho.infra.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.weslley.trabalho.doamin.Cliente;
import dev.weslley.trabalho.doamin.Fatura;
import dev.weslley.trabalho.doamin.Instalacao;
import dev.weslley.trabalho.infra.controller.exceptions.RecursoNotFoundException;
import dev.weslley.trabalho.infra.repository.ClienteRepository;
import dev.weslley.trabalho.infra.repository.FaturaRepository;
import dev.weslley.trabalho.infra.repository.InstalacaoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Acme AP Fatura Service", produces = MediaType.APPLICATION_JSON_VALUE)
public class FaturaController {

	@Autowired
	private FaturaRepository faturaRepository;

	@Autowired
	private InstalacaoRepository instalacaoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@ApiOperation(value = "Mostra a lista de faturas")
	// Controle de versão explicito na URI
	@GetMapping("v1/faturas")
	public List<Fatura> getAllFaturas() {

		return faturaRepository.findAll();
	}

	@ApiOperation(value = "Consulta uma fatura pelo código")
	@GetMapping("v1/faturas/{codigo}")
	public Fatura getFatura(@PathVariable String codigo) {
		Optional<Fatura> fatura = null;

		fatura = faturaRepository.findByCodigo(codigo);
		return fatura.orElseThrow(() -> new RecursoNotFoundException("codigo de fatura inválido - " + codigo));
	}

	@ApiOperation(value = "Consulta as faturas pelo CPF do cliente")
	@GetMapping("v1/faturas/cpf/{cpf}")
	public List<Fatura> getFaturasPorCPF(@PathVariable String cpf) {

		Optional<Cliente> cliente = clienteRepository.findByCpf(cpf);
		List<Instalacao> listaInstalacao = instalacaoRepository
				.findByCliente(cliente.orElseThrow(() -> new RecursoNotFoundException("CPF - " + cpf)));

		List<Fatura> listaFaturasCliente = new ArrayList<>();
		listaInstalacao.stream().forEach(item -> listaFaturasCliente.addAll(item.getListaFatura()));
		return listaFaturasCliente;
	}

	@ApiOperation(value = "Gerar uma nova fatura")
	@PostMapping("v1/faturas")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity gerarFatura(@RequestBody Fatura fatura) {

		Optional<Instalacao> instalacaoRecuperada;
		URI location = null;

		try {

			instalacaoRecuperada = instalacaoRepository.findByCodigo(fatura.getInstalacao().getCodigo());
			if (instalacaoRecuperada.get() == null)
				throw new RecursoNotFoundException("codigo instalacao - " + fatura.getInstalacao().getCodigo());

			fatura.setInstalacao(instalacaoRecuperada.get());

			Fatura faturaCriada = faturaRepository.save(fatura);
			location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(faturaCriada.getId()).toUri();

		} catch (Exception e) {
			// TODO: handle exception
			throw new RecursoNotFoundException(
					"Erro ao gerar fatura para a instalacao - " + fatura.getInstalacao().getCodigo());
		}

		return ResponseEntity.created(location).build();
	}

}