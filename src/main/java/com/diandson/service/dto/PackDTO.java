package com.diandson.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.Pack} entity.
 */
public class PackDTO implements Serializable {

    private Long id;

    private String libelle;

    private Long durer;

    private Boolean valide;

    private ZonedDateTime dateRenew;

    private StructureDTO operateur;

    private TypePackDTO type;

    private StructureDTO structure;

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

    public Boolean getValide() {
        return valide;
    }

    public void setValide(Boolean valide) {
        this.valide = valide;
    }

    public ZonedDateTime getDateRenew() {
        return dateRenew;
    }

    public void setDateRenew(ZonedDateTime dateRenew) {
        this.dateRenew = dateRenew;
    }

    public StructureDTO getOperateur() {
        return operateur;
    }

    public void setOperateur(StructureDTO operateur) {
        this.operateur = operateur;
    }

    public TypePackDTO getType() {
        return type;
    }

    public void setType(TypePackDTO type) {
        this.type = type;
    }

    public StructureDTO getStructure() {
        return structure;
    }

    public void setStructure(StructureDTO structure) {
        this.structure = structure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PackDTO)) {
            return false;
        }

        PackDTO packDTO = (PackDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, packDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PackDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", durer=" + getDurer() +
            ", valide='" + getValide() + "'" +
            ", dateRenew='" + getDateRenew() + "'" +
            ", operateur=" + getOperateur() +
            ", type=" + getType() +
            ", structure=" + getStructure() +
            "}";
    }
}
