package com.diandson.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.Fournisseur} entity.
 */
public class FournisseurDTO implements Serializable {

    private Long id;

    private String libelle;

    private String email;

    private PersonneDTO operateur;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PersonneDTO getOperateur() {
        return operateur;
    }

    public void setOperateur(PersonneDTO operateur) {
        this.operateur = operateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FournisseurDTO)) {
            return false;
        }

        FournisseurDTO fournisseurDTO = (FournisseurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fournisseurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FournisseurDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", email='" + getEmail() + "'" +
            ", operateur=" + getOperateur() +
            "}";
    }
}
