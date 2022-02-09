package com.diandson.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.ApprovisionnementMedicament} entity.
 */
public class ApprovisionnementMedicamentDTO implements Serializable {

    private Long id;

    private Long quantite;

    private MedicamentDTO medicament;

    private ApprovisionnementDTO approvionnement;

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

    public MedicamentDTO getMedicament() {
        return medicament;
    }

    public void setMedicament(MedicamentDTO medicament) {
        this.medicament = medicament;
    }

    public ApprovisionnementDTO getApprovionnement() {
        return approvionnement;
    }

    public void setApprovionnement(ApprovisionnementDTO approvionnement) {
        this.approvionnement = approvionnement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovisionnementMedicamentDTO)) {
            return false;
        }

        ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO = (ApprovisionnementMedicamentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, approvisionnementMedicamentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovisionnementMedicamentDTO{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", medicament=" + getMedicament() +
            ", approvionnement=" + getApprovionnement() +
            "}";
    }
}
