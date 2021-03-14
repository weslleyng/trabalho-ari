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
import dev.weslley.trabalho.infra.controller.exceptions.RecursoNotFoundException;
import dev.weslley.trabalho.infra.repository.ClienteRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Acme AP Cliente Service")
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;

	@ApiOperation(value = "Exibe a lista de clientes")
	@GetMapping("v1/clientes")
	public List<Cliente> getAllClientes() {
		return clienteRepository.findAll();
	}

	@ApiOperation(value = "Consulta um cliente pelo CPF")
	@GetMapping("v1/clientes/{cpf}")
	public Cliente getClienteByCpf(@PathVariable String cpf) {

		return clienteRepository.findByCpf(cpf)
				.orElseThrow(() -> new RecursoNotFoundException("CPF inválido - " + cpf));
	}

	@ApiOperation(value = "Cadastrar um novo cliente")
	@PostMapping("v1/clientes")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> criarCliente(@RequestBody Cliente cliente) {
		Cliente clienteCriado;

		URI location = null;

		try {
			clienteCriado = clienteRepository.save(cliente);

			location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(clienteCriado.getId()).toUri();
		} catch (Exception e) {
			// TODO: handle exception
			throw new RecursoNotFoundException("Erro ao cadastrar cliente CPF: " + cliente.getCpf());
		}
		return ResponseEntity.created(location).build();
	}

}
