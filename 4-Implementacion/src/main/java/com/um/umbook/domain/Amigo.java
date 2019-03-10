package com.um.umbook.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import com.um.umbook.domain.enumeration.Estado;

/**
 * A Amigo.
 */
@Entity
@Table(name = "amigo")
@Document(indexName = "amigo")
public class Amigo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;

    @ManyToOne
    @JsonIgnoreProperties("amigos")
    private User usuario;

    @ManyToOne
    @JsonIgnoreProperties("amigos")
    private User amigo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estado getEstado() {
        return estado;
    }

    public Amigo estado(Estado estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public User getUsuario() {
        return usuario;
    }

    public Amigo usuario(User user) {
        this.usuario = user;
        return this;
    }

    public void setUsuario(User user) {
        this.usuario = user;
    }

    public User getAmigo() {
        return amigo;
    }

    public Amigo amigo(User user) {
        this.amigo = user;
        return this;
    }

    public void setAmigo(User user) {
        this.amigo = user;
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
        Amigo amigo = (Amigo) o;
        if (amigo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), amigo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Amigo{" +
            "id=" + getId() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
