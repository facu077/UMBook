package com.um.umbook.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Album.
 */
@Entity
@Table(name = "album")
@Document(indexName = "album")
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "album")
    private Set<Foto> fotos = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("albums")
    private User usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Foto> getFotos() {
        return fotos;
    }

    public Album fotos(Set<Foto> fotos) {
        this.fotos = fotos;
        return this;
    }

    public Album addFotos(Foto foto) {
        this.fotos.add(foto);
        foto.setAlbum(this);
        return this;
    }

    public Album removeFotos(Foto foto) {
        this.fotos.remove(foto);
        foto.setAlbum(null);
        return this;
    }

    public void setFotos(Set<Foto> fotos) {
        this.fotos = fotos;
    }

    public User getUsuario() {
        return usuario;
    }

    public Album usuario(User user) {
        this.usuario = user;
        return this;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Album album = (Album) o;
        if (album.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), album.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Album{" +
            "id=" + getId() +
            "}";
    }
}
