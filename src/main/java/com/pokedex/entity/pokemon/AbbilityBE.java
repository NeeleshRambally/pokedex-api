package com.pokedex.entity.pokemon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "abbility")
public class AbbilityBE implements Serializable {

    private static final long serialVersionUID = 5195976981283679647L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "abbilityName")
    private String abbilityName;
    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ToString.Exclude
    private PokemonBE pokemon;
}
