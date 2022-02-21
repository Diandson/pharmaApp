package com.diandson.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MotifListeDepense.
 */
@Entity
@Table(name = "motif_liste_depense")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MotifListeDepense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "montant")
    private Long montant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MotifListeDepense id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public MotifListeDepense libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Long getMontant() {
        return this.montant;
    }

    public MotifListeDepense montant(Long montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MotifListeDepense)) {
            return false;
        }
        return id != null && id.equals(((MotifListeDepense) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MotifListeDepense{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", montant=" + getMontant() +
            "}";
    }
}
