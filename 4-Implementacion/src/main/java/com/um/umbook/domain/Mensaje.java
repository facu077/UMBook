package com.um.umbook.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Mensaje.
 */
@Entity
@Table(name = "mensaje")
@Document(indexName = "mensaje")
public class Mensaje implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "texto")
    private String texto;

    @Column(name = "fecha")
    private Instant fecha;

    @ManyToOne
    @JsonIgnoreProperties("mensajes")
    private Muro muro;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public Mensaje texto(String texto) {
        this.texto = texto;
        return this;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Instant getFecha() {
        return fecha;
    }

    public Mensaje fecha(Instant fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Muro getMuro() {
        return muro;
    }

    public Mensaje muro(Muro muro) {
        this.muro = muro;
        return this;
    }

    public void setMuro(Muro muro) {
        this.muro = muro;
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
        Mensaje mensaje = (Mensaje) o;
        if (mensaje.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), mensaje.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Mensaje{" +
            "id=" + getId() +
            ", texto='" + getTexto() + "'" +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
