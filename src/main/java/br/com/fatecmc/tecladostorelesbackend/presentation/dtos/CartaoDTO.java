package br.com.fatecmc.tecladostorelesbackend.presentation.dtos;

import br.com.fatecmc.tecladostorelesbackend.domain.models.enums.cartoes.Bandeiras;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartaoDTO {
    private Long id;
    private Boolean preferencial;
    private String numeros;
    private Bandeiras bandeira;
    private String nomeImpresso;
    private String codigoSeguranca;
    private Long clienteId;
}
