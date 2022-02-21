package com.diandson.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.Depense} entity.
 */
public class DepenseDTO implements Serializable {

    private Long id;

    private String numero;

    private String motifDepense;

    private String ordonnateur;

    private String justificatif;

    private Long montant;

    private ZonedDateTime dateDepense;

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

    public String getMotifDepense() {
        return motifDepense;
    }

    public void setMotifDepense(String motifDepense) {
        this.motifDepense = motifDepense;
    }

    public String getOrdonnateur() {
        return ordonnateur;
    }

    public void setOrdonnateur(String ordonnateur) {
        this.ordonnateur = ordonnateur;
    }

    public String getJustificatif() {
        return justificatif;
    }

    public void setJustificatif(String justificatif) {
        this.justificatif = justificatif;
    }

    public Long getMontant() {
        return montant;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public ZonedDateTime getDateDepense() {
        return dateDepense;
    }

    public void setDateDepense(ZonedDateTime dateDepense) {
        this.dateDepense = dateDepense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepenseDTO)) {
            return false;
        }

        DepenseDTO depenseDTO = (DepenseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depenseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepenseDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", motifDepense='" + getMotifDepense() + "'" +
            ", ordonnateur='" + getOrdonnateur() + "'" +
            ", justificatif='" + getJustificatif() + "'" +
            ", montant=" + getMontant() +
            ", dateDepense='" + getDateDepense() + "'" +
            "}";
    }
}
