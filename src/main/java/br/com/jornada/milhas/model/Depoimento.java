package br.com.jornada.milhas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "depoimentos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Depoimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String foto;

    private String depoimento;

    private String nomePessoa;

    public Depoimento(Depoimento dados) {

    }
}
