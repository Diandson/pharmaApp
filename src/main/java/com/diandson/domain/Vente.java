package com.diandson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vente.
 */
@Entity
@Table(name = "vente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "date_vente")
    private ZonedDateTime dateVente;

    @Column(name = "montant")
    private Long montant;

    @Column(name = "montant_assurance")
    private Long montantAssurance;

    @Column(name = "somme_donne")
    private Long sommeDonne;

    @Column(name = "somme_rendu")
    private Long sommeRendu;

    @Column(name = "avoir")
    private Long avoir;

    @OneToMany(mappedBy = "vente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "medicament", "vente" }, allowSetters = true)
    private Set<VenteMedicament> venteMedicaments = new HashSet<>();

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

    public Vente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Vente numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public ZonedDateTime getDateVente() {
        return this.dateVente;
    }

    public Vente dateVente(ZonedDateTime dateVente) {
        this.setDateVente(dateVente);
        return this;
    }

    public void setDateVente(ZonedDateTime dateVente) {
        this.dateVente = dateVente;
    }

    public Long getMontant() {
        return this.montant;
    }

    public Vente montant(Long montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public Long getMontantAssurance() {
        return this.montantAssurance;
    }

    public Vente montantAssurance(Long montantAssurance) {
        this.setMontantAssurance(montantAssurance);
        return this;
    }

    public void setMontantAssurance(Long montantAssurance) {
        this.montantAssurance = montantAssurance;
    }

    public Long getSommeDonne() {
        return this.sommeDonne;
    }

    public Vente sommeDonne(Long sommeDonne) {
        this.setSommeDonne(sommeDonne);
        return this;
    }

    public void setSommeDonne(Long sommeDonne) {
        this.sommeDonne = sommeDonne;
    }

    public Long getSommeRendu() {
        return this.sommeRendu;
    }

    public Vente sommeRendu(Long sommeRendu) {
        this.setSommeRendu(sommeRendu);
        return this;
    }

    public void setSommeRendu(Long sommeRendu) {
        this.sommeRendu = sommeRendu;
    }

    public Long getAvoir() {
        return this.avoir;
    }

    public Vente avoir(Long avoir) {
        this.setAvoir(avoir);
        return this;
    }

    public void setAvoir(Long avoir) {
        this.avoir = avoir;
    }

    public Set<VenteMedicament> getVenteMedicaments() {
        return this.venteMedicaments;
    }

    public void setVenteMedicaments(Set<VenteMedicament> venteMedicaments) {
        if (this.venteMedicaments != null) {
            this.venteMedicaments.forEach(i -> i.setVente(null));
        }
        if (venteMedicaments != null) {
            venteMedicaments.forEach(i -> i.setVente(this));
        }
        this.venteMedicaments = venteMedicaments;
    }

    public Vente venteMedicaments(Set<VenteMedicament> venteMedicaments) {
        this.setVenteMedicaments(venteMedicaments);
        return this;
    }

    public Vente addVenteMedicament(VenteMedicament venteMedicament) {
        this.venteMedicaments.add(venteMedicament);
        venteMedicament.setVente(this);
        return this;
    }

    public Vente removeVenteMedicament(VenteMedicament venteMedicament) {
        this.venteMedicaments.remove(venteMedicament);
        venteMedicament.setVente(null);
        return this;
    }

    public Personne getOperateur() {
        return this.operateur;
    }

    public void setOperateur(Personne personne) {
        this.operateur = personne;
    }

    public Vente operateur(Personne personne) {
        this.setOperateur(personne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vente)) {
            return false;
        }
        return id != null && id.equals(((Vente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vente{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dateVente='" + getDateVente() + "'" +
            ", montant=" + getMontant() +
            ", montantAssurance=" + getMontantAssurance() +
            ", sommeDonne=" + getSommeDonne() +
            ", sommeRendu=" + getSommeRendu() +
            ", avoir=" + getAvoir() +
            "}";
    }
}
