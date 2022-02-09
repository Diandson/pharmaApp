package com.diandson.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.TypePack} entity.
 */
public class TypePackDTO implements Serializable {

    private Long id;

    private String libelle;

    private Long durer;

    private Long prix;

    private Long annexe;

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

    public Long getDurer() {
        return durer;
    }

    public void setDurer(Long durer) {
        this.durer = durer;
    }

    public Long getPrix() {
        return prix;
    }

    public void setPrix(Long prix) {
        this.prix = prix;
    }

    public Long getAnnexe() {
        return annexe;
    }

    public void setAnnexe(Long annexe) {
        this.annexe = annexe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypePackDTO)) {
            return false;
        }

        TypePackDTO typePackDTO = (TypePackDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typePackDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypePackDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", durer=" + getDurer() +
            ", prix=" + getPrix() +
            ", annexe=" + getAnnexe() +
            "}";
    }
}
