package com.diandson.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.diandson.domain.Assurance} entity.
 */
public class AssuranceDTO implements Serializable {

    private Long id;

    @NotNull
    private String libelle;

    private Long taux;

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

    public Long getTaux() {
        return taux;
    }

    public void setTaux(Long taux) {
        this.taux = taux;
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
        if (!(o instanceof AssuranceDTO)) {
            return false;
        }

        AssuranceDTO assuranceDTO = (AssuranceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assuranceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssuranceDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", taux=" + getTaux() +
            ", email='" + getEmail() + "'" +
            ", operateur=" + getOperateur() +
            "}";
    }
}
