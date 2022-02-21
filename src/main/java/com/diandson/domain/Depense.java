package com.diandson.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Depense.
 */
@Entity
@Table(name = "depense")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Depense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "motif_depense")
    private String motifDepense;

    @Column(name = "ordonnateur")
    private String ordonnateur;

    @Column(name = "justificatif")
    private String justificatif;

    @Column(name = "montant")
    private Long montant;

    @Column(name = "date_depense")
    private ZonedDateTime dateDepense;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Depense id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Depense numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getMotifDepense() {
        return this.motifDepense;
    }

    public Depense motifDepense(String motifDepense) {
        this.setMotifDepense(motifDepense);
        return this;
    }

    public void setMotifDepense(String motifDepense) {
        this.motifDepense = motifDepense;
    }

    public String getOrdonnateur() {
        return this.ordonnateur;
    }

    public Depense ordonnateur(String ordonnateur) {
        this.setOrdonnateur(ordonnateur);
        return this;
    }

    public void setOrdonnateur(String ordonnateur) {
        this.ordonnateur = ordonnateur;
    }

    public String getJustificatif() {
        return this.justificatif;
    }

    public Depense justificatif(String justificatif) {
        this.setJustificatif(justificatif);
        return this;
    }

    public void setJustificatif(String justificatif) {
        this.justificatif = justificatif;
    }

    public Long getMontant() {
        return this.montant;
    }

    public Depense montant(Long montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public ZonedDateTime getDateDepense() {
        return this.dateDepense;
    }

    public Depense dateDepense(ZonedDateTime dateDepense) {
        this.setDateDepense(dateDepense);
        return this;
    }

    public void setDateDepense(ZonedDateTime dateDepense) {
        this.dateDepense = dateDepense;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Depense)) {
            return false;
        }
        return id != null && id.equals(((Depense) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Depense{" +
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
