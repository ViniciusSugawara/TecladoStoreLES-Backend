package br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output;

import br.com.fatecmc.tecladostorelesbackend.domain.models.Cliente;
import br.com.fatecmc.tecladostorelesbackend.domain.models.enums.enderecos.TiposEndereco;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO extends BaseDTO {
    private String nomeEndereco;
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
