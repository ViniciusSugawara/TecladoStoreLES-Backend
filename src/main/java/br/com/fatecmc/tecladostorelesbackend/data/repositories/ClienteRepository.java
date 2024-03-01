package br.com.fatecmc.tecladostorelesbackend.data.repositories;

import br.com.fatecmc.tecladostorelesbackend.domain.models.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
