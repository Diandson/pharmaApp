package com.diandson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VenteMedicament.
 */
@Entity
@Table(name = "vente_medicament")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VenteMedicament implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quantite")
    private Long quantite;

    @Column(name = "montant")
    private String montant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "structure" }, allowSetters = true)
    private Medicament medicament;

    @ManyToOne
    @JsonIgnoreProperties(value = { "venteMedicaments", "operateur" }, allowSetters = true)
    private Vente vente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VenteMedicament id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantite() {
        return this.quantite;
    }

    public VenteMedicament quantite(Long quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public String getMontant() {
        return this.montant;
    }

    public VenteMedicament montant(String montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public Medicament getMedicament() {
        return this.medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public VenteMedicament medicament(Medicament medicament) {
        this.setMedicament(medicament);
        return this;
    }

    public Vente getVente() {
        return this.vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public VenteMedicament vente(Vente vente) {
        this.setVente(vente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VenteMedicament)) {
            return false;
        }
        return id != null && id.equals(((VenteMedicament) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VenteMedicament{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", montant='" + getMontant() + "'" +
            "}";
    }
}
