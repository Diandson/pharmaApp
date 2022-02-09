package com.diandson.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.VenteMedicament} entity.
 */
public class VenteMedicamentDTO implements Serializable {

    private Long id;

    private Long quantite;

    private String montant;

    private MedicamentDTO medicament;

    private VenteDTO vente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantite() {
        return quantite;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public MedicamentDTO getMedicament() {
        return medicament;
    }

    public void setMedicament(MedicamentDTO medicament) {
        this.medicament = medicament;
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
        if (!(o instanceof VenteMedicamentDTO)) {
            return false;
        }

        VenteMedicamentDTO venteMedicamentDTO = (VenteMedicamentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, venteMedicamentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VenteMedicamentDTO{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", montant='" + getMontant() + "'" +
            ", medicament=" + getMedicament() +
            ", vente=" + getVente() +
            "}";
    }
}
