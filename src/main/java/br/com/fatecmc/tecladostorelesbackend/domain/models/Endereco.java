package br.com.fatecmc.tecladostorelesbackend.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "enderecos")
public class Endereco extends BaseModel {
    private String nomeEndereco;
    private String tipoResidencia;
    private String tipoLogradouro;
    private String logradouro;
    private String numero;
    private String bairro;
    private String cep;
    private String cidade;
    private String estado;
    private String pais;
    private String observacoes;
    @ManyToOne(cascade = CascadeType.ALL)
    private Cliente cliente;

    public Endereco(String nomeEndereco, String tipoResidencia, String tipoLogradouro, String logradouro, String numero, String bairro, String cep, String cidade, String estado, String pais, String observacoes) {
        this.nomeEndereco = nomeEndereco;
        this.tipoResidencia = tipoResidencia;
        this.tipoLogradouro = tipoLogradouro;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cep = cep;
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
        this.observacoes = observacoes;
    }
}
