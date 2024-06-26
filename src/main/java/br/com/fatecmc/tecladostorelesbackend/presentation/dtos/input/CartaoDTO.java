package br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input;

import br.com.fatecmc.tecladostorelesbackend.domain.models.cliente.enums.cartoes.Bandeiras;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartaoDTO extends BaseDTO {
    private Boolean preferencial;
    private String numeros;
    private Bandeiras bandeira;
    private String nomeImpresso;
    private String codigoSeguranca;
}
