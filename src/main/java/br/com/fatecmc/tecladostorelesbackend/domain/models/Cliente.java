package br.com.fatecmc.tecladostorelesbackend.domain.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "clientes")
public class Cliente extends BaseModel {
    private String nome;
    private Date dataNascimento;
    private String genero;
    private String cpf;
    private String telefone;
    private String email;
    private String senha;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id")
    private Set<Endereco> enderecos = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id")
    private Set<Cartao> cartoesCredito = new HashSet<>();
}