package com.diandson.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.Inventaire} entity.
 */
public class InventaireDTO implements Serializable {

    private Long id;

    private String numero;

    private String dateInventaire;

    private PersonneDTO operateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDateInventaire() {
        return dateInventaire;
    }

    public void setDateInventaire(String dateInventaire) {
        this.dateInventaire = dateInventaire;
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
        if (!(o instanceof InventaireDTO)) {
            return false;
        }

        InventaireDTO inventaireDTO = (InventaireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inventaireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventaireDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dateInventaire='" + getDateInventaire() + "'" +
            ", operateur=" + getOperateur() +
            "}";
    }
}
