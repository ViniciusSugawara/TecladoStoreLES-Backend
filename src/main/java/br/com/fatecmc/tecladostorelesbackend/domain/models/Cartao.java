package br.com.fatecmc.tecladostorelesbackend.domain.models;

import br.com.fatecmc.tecladostorelesbackend.domain.models.enums.cartoes.Bandeiras;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "cartoes")
public class Cartao extends BaseModel{

    private Boolean preferencial;
    private String numeros;
    @Enumerated(EnumType.STRING)
    private Bandeiras bandeira;
    private String nomeImpresso;
    private String codigoSeguranca;
    @ManyToOne()
    private Cliente clienteResponsavel;
}
