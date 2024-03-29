package br.com.fatecmc.tecladostorelesbackend.data.repositories;

import br.com.fatecmc.tecladostorelesbackend.domain.models.cliente.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
}
