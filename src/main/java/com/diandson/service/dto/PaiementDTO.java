package com.diandson.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.Paiement} entity.
 */
public class PaiementDTO implements Serializable {

    private Long id;

    private String numero;

    private String numeroVente;

    private ZonedDateTime datePaiement;

    private Long sommeRecu;

    private Long sommeDonner;

    private Long avoir;

    private VersementDTO versement;

    private PersonneDTO operateur;

    private VenteDTO vente;

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

    public String getNumeroVente() {
        return numeroVente;
    }

    public void setNumeroVente(String numeroVente) {
        this.numeroVente = numeroVente;
    }

    public ZonedDateTime getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(ZonedDateTime datePaiement) {
        this.datePaiement = datePaiement;
    }

    public Long getSommeRecu() {
        return sommeRecu;
    }

    public void setSommeRecu(Long sommeRecu) {
        this.sommeRecu = sommeRecu;
    }

    public Long getSommeDonner() {
        return sommeDonner;
    }

    public void setSommeDonner(Long sommeDonner) {
        this.sommeDonner = sommeDonner;
    }

    public Long getAvoir() {
        return avoir;
    }

    public void setAvoir(Long avoir) {
        this.avoir = avoir;
    }

    public VersementDTO getVersement() {
        return versement;
    }

    public void setVersement(VersementDTO versement) {
        this.versement = versement;
    }

    public PersonneDTO getOperateur() {
        return operateur;
    }

    public void setOperateur(PersonneDTO operateur) {
        this.operateur = operateur;
    }

    public VenteDTO getVente() {
        return vente;
    }

    public void setVente(VenteDTO vente) {
        this.vente = vente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaiementDTO)) {
            return false;
        }

        PaiementDTO paiementDTO = (PaiementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paiementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", numeroVente='" + getNumeroVente() + "'" +
            ", datePaiement='" + getDatePaiement() + "'" +
            ", sommeRecu=" + getSommeRecu() +
            ", sommeDonner=" + getSommeDonner() +
            ", avoir=" + getAvoir() +
            ", versement=" + getVersement() +
            ", operateur=" + getOperateur() +
            ", vente=" + getVente() +
            "}";
    }
}
