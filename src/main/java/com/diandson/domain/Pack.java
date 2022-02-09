package com.diandson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pack.
 */
@Entity
@Table(name = "pack")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pack implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "durer")
    private Long durer;

    @Column(name = "valide")
    private Boolean valide;

    @Column(name = "date_renew")
    private ZonedDateTime dateRenew;

    @ManyToOne
    @JsonIgnoreProperties(value = { "packs", "medicaments", "personnes" }, allowSetters = true)
    private Structure operateur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "packs" }, allowSetters = true)
    private TypePack type;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pack id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Pack libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Long getDurer() {
        return this.durer;
    }

    public Pack durer(Long durer) {
        this.setDurer(durer);
        return this;
    }

    public void setDurer(Long durer) {
        this.durer = durer;
    }

    public Boolean getValide() {
        return this.valide;
    }

    public Pack valide(Boolean valide) {
        this.setValide(valide);
        return this;
    }

    public void setValide(Boolean valide) {
        this.valide = valide;
    }

    public ZonedDateTime getDateRenew() {
        return this.dateRenew;
    }

    public Pack dateRenew(ZonedDateTime dateRenew) {
        this.setDateRenew(dateRenew);
        return this;
    }

    public void setDateRenew(ZonedDateTime dateRenew) {
        this.dateRenew = dateRenew;
    }

    public Structure getOperateur() {
        return this.operateur;
    }

    public void setOperateur(Structure structure) {
        this.operateur = structure;
    }

    public Pack operateur(Structure structure) {
        this.setOperateur(structure);
        return this;
    }

    public TypePack getType() {
        return this.type;
    }

    public void setType(TypePack typePack) {
        this.type = typePack;
    }

    public Pack type(TypePack typePack) {
        this.setType(typePack);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pack)) {
            return false;
        }
        return id != null && id.equals(((Pack) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pack{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", durer=" + getDurer() +
            ", valide='" + getValide() + "'" +
            ", dateRenew='" + getDateRenew() + "'" +
            "}";
    }
}
