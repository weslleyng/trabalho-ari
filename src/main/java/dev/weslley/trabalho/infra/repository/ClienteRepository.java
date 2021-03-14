package dev.weslley.trabalho.infra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.weslley.trabalho.doamin.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	public Optional<Cliente> findByCpf(String cpf);
	
}