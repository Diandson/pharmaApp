package com.diandson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypePack.
 */
@Entity
@Table(name = "type_pack")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypePack implements Serializable {

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

    @Column(name = "prix")
    private Long prix;

    @Column(name = "annexe")
    private Long annexe;

    @OneToMany(mappedBy = "type")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "operateur", "type" }, allowSetters = true)
    private Set<Pack> packs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypePack id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public TypePack libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Long getDurer() {
        return this.durer;
    }

    public TypePack durer(Long durer) {
        this.setDurer(durer);
        return this;
    }

    public void setDurer(Long durer) {
        this.durer = durer;
    }

    public Long getPrix() {
        return this.prix;
    }

    public TypePack prix(Long prix) {
        this.setPrix(prix);
        return this;
    }

    public void setPrix(Long prix) {
        this.prix = prix;
    }

    public Long getAnnexe() {
        return this.annexe;
    }

    public TypePack annexe(Long annexe) {
        this.setAnnexe(annexe);
        return this;
    }

    public void setAnnexe(Long annexe) {
        this.annexe = annexe;
    }

    public Set<Pack> getPacks() {
        return this.packs;
    }

    public void setPacks(Set<Pack> packs) {
        if (this.packs != null) {
            this.packs.forEach(i -> i.setType(null));
        }
        if (packs != null) {
            packs.forEach(i -> i.setType(this));
        }
        this.packs = packs;
    }

    public TypePack packs(Set<Pack> packs) {
        this.setPacks(packs);
        return this;
    }

    public TypePack addPack(Pack pack) {
        this.packs.add(pack);
        pack.setType(this);
        return this;
    }

    public TypePack removePack(Pack pack) {
        this.packs.remove(pack);
        pack.setType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypePack)) {
            return false;
        }
        return id != null && id.equals(((TypePack) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypePack{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", durer=" + getDurer() +
            ", prix=" + getPrix() +
            ", annexe=" + getAnnexe() +
            "}";
    }
}
