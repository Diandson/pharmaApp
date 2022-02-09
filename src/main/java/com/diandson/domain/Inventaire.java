package com.diandson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Inventaire.
 */
@Entity
@Table(name = "inventaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Inventaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "date_inventaire")
    private String dateInventaire;

    @OneToMany(mappedBy = "inventaire")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "medicament", "inventaire" }, allowSetters = true)
    private Set<InventaireMedicament> inventaireMedicaments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "user",
            "commandes",
            "assurances",
            "ventes",
            "fournisseurs",
            "clients",
            "livraisons",
            "inventaires",
            "messages",
            "notifications",
            "structure",
        },
        allowSetters = true
    )
    private Personne operateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inventaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Inventaire numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDateInventaire() {
        return this.dateInventaire;
    }

    public Inventaire dateInventaire(String dateInventaire) {
        this.setDateInventaire(dateInventaire);
        return this;
    }

    public void setDateInventaire(String dateInventaire) {
        this.dateInventaire = dateInventaire;
    }

    public Set<InventaireMedicament> getInventaireMedicaments() {
        return this.inventaireMedicaments;
    }

    public void setInventaireMedicaments(Set<InventaireMedicament> inventaireMedicaments) {
        if (this.inventaireMedicaments != null) {
            this.inventaireMedicaments.forEach(i -> i.setInventaire(null));
        }
        if (inventaireMedicaments != null) {
            inventaireMedicaments.forEach(i -> i.setInventaire(this));
        }
        this.inventaireMedicaments = inventaireMedicaments;
    }

    public Inventaire inventaireMedicaments(Set<InventaireMedicament> inventaireMedicaments) {
        this.setInventaireMedicaments(inventaireMedicaments);
        return this;
    }

    public Inventaire addInventaireMedicament(InventaireMedicament inventaireMedicament) {
        this.inventaireMedicaments.add(inventaireMedicament);
        inventaireMedicament.setInventaire(this);
        return this;
    }

    public Inventaire removeInventaireMedicament(InventaireMedicament inventaireMedicament) {
        this.inventaireMedicaments.remove(inventaireMedicament);
        inventaireMedicament.setInventaire(null);
        return this;
    }

    public Personne getOperateur() {
        return this.operateur;
    }

    public void setOperateur(Personne personne) {
        this.operateur = personne;
    }

    public Inventaire operateur(Personne personne) {
        this.setOperateur(personne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inventaire)) {
            return false;
        }
        return id != null && id.equals(((Inventaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inventaire{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dateInventaire='" + getDateInventaire() + "'" +
            "}";
    }
}
