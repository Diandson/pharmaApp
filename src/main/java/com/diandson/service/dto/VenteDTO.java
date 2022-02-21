package com.diandson.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.Vente} entity.
 */
public class VenteDTO implements Serializable {

    private Long id;

    private String numero;

    private ZonedDateTime dateVente;

    private Long montant;

    private Long montantAssurance;

    private Long sommeDonne;

    private Long sommeRendu;

    private Long avoir;

    private PersonneDTO operateur;

    private List<MedicamentDTO> medicament;

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

    public ZonedDateTime getDateVente() {
        return dateVente;
    }

    public void setDateVente(ZonedDateTime dateVente) {
        this.dateVente = dateVente;
    }

    public Long getMontant() {
        return montant;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public Long getMontantAssurance() {
        return montantAssurance;
    }

    public void setMontantAssurance(Long montantAssurance) {
        this.montantAssurance = montantAssurance;
    }

    public Long getSommeDonne() {
        return sommeDonne;
    }

    public void setSommeDonne(Long sommeDonne) {
        this.sommeDonne = sommeDonne;
    }

    public Long getSommeRendu() {
        return sommeRendu;
    }

    public void setSommeRendu(Long sommeRendu) {
        this.sommeRendu = sommeRendu;
    }

    public Long getAvoir() {
        return avoir;
    }

    public void setAvoir(Long avoir) {
        this.avoir = avoir;
    }

    public PersonneDTO getOperateur() {
        return operateur;
    }

    public void setOperateur(PersonneDTO operateur) {
        this.operateur = operateur;
    }

    public List<MedicamentDTO> getMedicament() {
        return medicament;
    }

    public void setMedicament(List<MedicamentDTO> medicament) {
        this.medicament = medicament;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VenteDTO)) {
            return false;
        }

        VenteDTO venteDTO = (VenteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, venteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VenteDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dateVente='" + getDateVente() + "'" +
            ", montant=" + getMontant() +
            ", montantAssurance=" + getMontantAssurance() +
            ", sommeDonne=" + getSommeDonne() +
            ", sommeRendu=" + getSommeRendu() +
            ", avoir=" + getAvoir() +
            ", operateur=" + getOperateur() +
            "}";
    }
}
