package com.diandson.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.InventaireMedicament} entity.
 */
public class InventaireMedicamentDTO implements Serializable {

    private Long id;

    private Long stockTheorique;

    private Long stockPhysique;

    private Long stockDifferent;

    private LocalDate dateFabrication;

    private LocalDate dateExpiration;

    private MedicamentDTO medicament;

    private InventaireDTO inventaire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockTheorique() {
        return stockTheorique;
    }

    public void setStockTheorique(Long stockTheorique) {
        this.stockTheorique = stockTheorique;
    }

    public Long getStockPhysique() {
        return stockPhysique;
    }

    public void setStockPhysique(Long stockPhysique) {
        this.stockPhysique = stockPhysique;
    }

    public Long getStockDifferent() {
        return stockDifferent;
    }

    public void setStockDifferent(Long stockDifferent) {
        this.stockDifferent = stockDifferent;
    }

    public LocalDate getDateFabrication() {
        return dateFabrication;
    }

    public void setDateFabrication(LocalDate dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public MedicamentDTO getMedicament() {
        return medicament;
    }

    public void setMedicament(MedicamentDTO medicament) {
        this.medicament = medicament;
    }

    public InventaireDTO getInventaire() {
        return inventaire;
    }

    public void setInventaire(InventaireDTO inventaire) {
        this.inventaire = inventaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventaireMedicamentDTO)) {
            return false;
        }

        InventaireMedicamentDTO inventaireMedicamentDTO = (InventaireMedicamentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inventaireMedicamentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventaireMedicamentDTO{" +
            "id=" + getId() +
            ", stockTheorique=" + getStockTheorique() +
            ", stockPhysique=" + getStockPhysique() +
            ", stockDifferent=" + getStockDifferent() +
            ", dateFabrication='" + getDateFabrication() + "'" +
            ", dateExpiration='" + getDateExpiration() + "'" +
            ", medicament=" + getMedicament() +
            ", inventaire=" + getInventaire() +
            "}";
    }
}
