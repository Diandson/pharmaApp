package com.diandson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InventaireMedicament.
 */
@Entity
@Table(name = "inventaire_medicament")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InventaireMedicament implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "stock_theorique")
    private Long stockTheorique;

    @Column(name = "stock_physique")
    private Long stockPhysique;

    @Column(name = "stock_different")
    private Long stockDifferent;

    @Column(name = "date_fabrication")
    private LocalDate dateFabrication;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @ManyToOne
    @JsonIgnoreProperties(value = { "structure" }, allowSetters = true)
    private Medicament medicament;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inventaireMedicaments", "operateur" }, allowSetters = true)
    private Inventaire inventaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InventaireMedicament id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockTheorique() {
        return this.stockTheorique;
    }

    public InventaireMedicament stockTheorique(Long stockTheorique) {
        this.setStockTheorique(stockTheorique);
        return this;
    }

    public void setStockTheorique(Long stockTheorique) {
        this.stockTheorique = stockTheorique;
    }

    public Long getStockPhysique() {
        return this.stockPhysique;
    }

    public InventaireMedicament stockPhysique(Long stockPhysique) {
        this.setStockPhysique(stockPhysique);
        return this;
    }

    public void setStockPhysique(Long stockPhysique) {
        this.stockPhysique = stockPhysique;
    }

    public Long getStockDifferent() {
        return this.stockDifferent;
    }

    public InventaireMedicament stockDifferent(Long stockDifferent) {
        this.setStockDifferent(stockDifferent);
        return this;
    }

    public void setStockDifferent(Long stockDifferent) {
        this.stockDifferent = stockDifferent;
    }

    public LocalDate getDateFabrication() {
        return this.dateFabrication;
    }

    public InventaireMedicament dateFabrication(LocalDate dateFabrication) {
        this.setDateFabrication(dateFabrication);
        return this;
    }

    public void setDateFabrication(LocalDate dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    public LocalDate getDateExpiration() {
        return this.dateExpiration;
    }

    public InventaireMedicament dateExpiration(LocalDate dateExpiration) {
        this.setDateExpiration(dateExpiration);
        return this;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Medicament getMedicament() {
        return this.medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public InventaireMedicament medicament(Medicament medicament) {
        this.setMedicament(medicament);
        return this;
    }

    public Inventaire getInventaire() {
        return this.inventaire;
    }

    public void setInventaire(Inventaire inventaire) {
        this.inventaire = inventaire;
    }

    public InventaireMedicament inventaire(Inventaire inventaire) {
        this.setInventaire(inventaire);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventaireMedicament)) {
            return false;
        }
        return id != null && id.equals(((InventaireMedicament) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventaireMedicament{" +
            "id=" + getId() +
            ", stockTheorique=" + getStockTheorique() +
            ", stockPhysique=" + getStockPhysique() +
            ", stockDifferent=" + getStockDifferent() +
            ", dateFabrication='" + getDateFabrication() + "'" +
            ", dateExpiration='" + getDateExpiration() + "'" +
            "}";
    }
}
