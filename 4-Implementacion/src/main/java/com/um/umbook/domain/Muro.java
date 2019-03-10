package com.um.umbook.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Muro.
 */
@Entity
@Table(name = "muro")
@Document(indexName = "muro")
public class Muro implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User usuario;

    @OneToMany(mappedBy = "muro")
    private Set<Mensaje> mensajes = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUsuario() {
        return usuario;
    }

    public Muro usuario(User user) {
        this.usuario = user;
        return this;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public Set<Mensaje> getMensajes() {
        return mensajes;
    }

    public Muro mensajes(Set<Mensaje> mensajes) {
        this.mensajes = mensajes;
        return this;
    }

    public Muro addMensajes(Mensaje mensaje) {
        this.mensajes.add(mensaje);
        mensaje.setMuro(this);
        return this;
    }

    public Muro removeMensajes(Mensaje mensaje) {
        this.mensajes.remove(mensaje);
        mensaje.setMuro(null);
        return this;
    }

    public void setMensajes(Set<Mensaje> mensajes) {
        this.mensajes = mensajes;
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
        Muro muro = (Muro) o;
        if (muro.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), muro.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Muro{" +
            "id=" + getId() +
            "}";
    }
}
