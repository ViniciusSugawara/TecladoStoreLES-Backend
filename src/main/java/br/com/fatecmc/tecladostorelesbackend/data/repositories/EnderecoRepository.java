package br.com.fatecmc.tecladostorelesbackend.data.repositories;

import br.com.fatecmc.tecladostorelesbackend.domain.models.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
