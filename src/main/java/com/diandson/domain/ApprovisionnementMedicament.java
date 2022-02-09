package com.diandson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApprovisionnementMedicament.
 */
@Entity
@Table(name = "approvisionnement_medicament")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApprovisionnementMedicament implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quantite")
    private Long quantite;

    @ManyToOne
    @JsonIgnoreProperties(value = { "structure" }, allowSetters = true)
    private Medicament medicament;

    @ManyToOne
    @JsonIgnoreProperties(value = { "approvisionnementMedicaments" }, allowSetters = true)
    private Approvisionnement approvionnement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApprovisionnementMedicament id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantite() {
        return this.quantite;
    }

    public ApprovisionnementMedicament quantite(Long quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public Medicament getMedicament() {
        return this.medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public ApprovisionnementMedicament medicament(Medicament medicament) {
        this.setMedicament(medicament);
        return this;
    }

    public Approvisionnement getApprovionnement() {
        return this.approvionnement;
    }

    public void setApprovionnement(Approvisionnement approvisionnement) {
        this.approvionnement = approvisionnement;
    }

    public ApprovisionnementMedicament approvionnement(Approvisionnement approvisionnement) {
        this.setApprovionnement(approvisionnement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovisionnementMedicament)) {
            return false;
        }
        return id != null && id.equals(((ApprovisionnementMedicament) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovisionnementMedicament{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            "}";
    }
}
