package com.pokedex.entity.pokemon;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "pokemon")
public class PokemonBE implements Serializable {

    private static final long serialVersionUID = 5195976981283679647L;

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String imageURL;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "pokemon", fetch = FetchType.LAZY)
    private Set<MoveBE> moves = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "pokemon", fetch = FetchType.LAZY)
    private Set<TypeBE> types = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "pokemon", fetch = FetchType.LAZY)
    private Set<AbbilityBE> abbilities = new HashSet<>();


}
