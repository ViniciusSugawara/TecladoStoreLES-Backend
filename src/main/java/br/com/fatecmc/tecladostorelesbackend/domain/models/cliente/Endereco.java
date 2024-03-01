package br.com.fatecmc.tecladostorelesbackend.domain.models.cliente;

import br.com.fatecmc.tecladostorelesbackend.domain.models.BaseModel;
import br.com.fatecmc.tecladostorelesbackend.domain.models.cliente.enums.enderecos.TiposEndereco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "enderecos")
public class Endereco extends BaseModel {
    private String nomeEndereco;
    @Enumerated(EnumType.STRING)
    private TiposEndereco tipoEndereco;
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

}
