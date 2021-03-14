package dev.weslley.trabalho.infra.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.weslley.trabalho.doamin.Cliente;
import dev.weslley.trabalho.doamin.Instalacao;

public interface InstalacaoRepository extends JpaRepository<Instalacao, Long> {

	public Optional<Instalacao> findByCodigo(String codigo);

	public List<Instalacao> findByCliente(Cliente cliente);

}
