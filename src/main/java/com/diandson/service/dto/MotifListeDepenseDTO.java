package com.diandson.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.MotifListeDepense} entity.
 */
public class MotifListeDepenseDTO implements Serializable {

    private Long id;

    private String libelle;

    private Long montant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Long getMontant() {
        return montant;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MotifListeDepenseDTO)) {
            return false;
        }

        MotifListeDepenseDTO motifListeDepenseDTO = (MotifListeDepenseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, motifListeDepenseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MotifListeDepenseDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", montant=" + getMontant() +
            "}";
    }
}
