package com.diandson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Paiement.
 */
@Entity
@Table(name = "paiement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Paiement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "numero_vente")
    private String numeroVente;

    @Column(name = "date_paiement")
    private ZonedDateTime datePaiement;

    @Column(name = "somme_recu")
    private Long sommeRecu;

    @Column(name = "somme_donner")
    private Long sommeDonner;

    @Column(name = "avoir")
    private Long avoir;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paiements", "operateur" }, allowSetters = true)
    private Versement versement;

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

    @JsonIgnoreProperties(value = { "venteMedicaments", "operateur" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Vente vente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paiement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Paiement numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumeroVente() {
        return this.numeroVente;
    }

    public Paiement numeroVente(String numeroVente) {
        this.setNumeroVente(numeroVente);
        return this;
    }

    public void setNumeroVente(String numeroVente) {
        this.numeroVente = numeroVente;
    }

    public ZonedDateTime getDatePaiement() {
        return this.datePaiement;
    }

    public Paiement datePaiement(ZonedDateTime datePaiement) {
        this.setDatePaiement(datePaiement);
        return this;
    }

    public void setDatePaiement(ZonedDateTime datePaiement) {
        this.datePaiement = datePaiement;
    }

    public Long getSommeRecu() {
        return this.sommeRecu;
    }

    public Paiement sommeRecu(Long sommeRecu) {
        this.setSommeRecu(sommeRecu);
        return this;
    }

    public void setSommeRecu(Long sommeRecu) {
        this.sommeRecu = sommeRecu;
    }

    public Long getSommeDonner() {
        return this.sommeDonner;
    }

    public Paiement sommeDonner(Long sommeDonner) {
        this.setSommeDonner(sommeDonner);
        return this;
    }

    public void setSommeDonner(Long sommeDonner) {
        this.sommeDonner = sommeDonner;
    }

    public Long getAvoir() {
        return this.avoir;
    }

    public Paiement avoir(Long avoir) {
        this.setAvoir(avoir);
        return this;
    }

    public void setAvoir(Long avoir) {
        this.avoir = avoir;
    }

    public Versement getVersement() {
        return this.versement;
    }

    public void setVersement(Versement versement) {
        this.versement = versement;
    }

    public Paiement versement(Versement versement) {
        this.setVersement(versement);
        return this;
    }

    public Personne getOperateur() {
        return this.operateur;
    }

    public void setOperateur(Personne personne) {
        this.operateur = personne;
    }

    public Paiement operateur(Personne personne) {
        this.setOperateur(personne);
        return this;
    }

    public Vente getVente() {
        return this.vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public Paiement vente(Vente vente) {
        this.setVente(vente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paiement)) {
            return false;
        }
        return id != null && id.equals(((Paiement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paiement{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", numeroVente='" + getNumeroVente() + "'" +
            ", datePaiement='" + getDatePaiement() + "'" +
            ", sommeRecu=" + getSommeRecu() +
            ", sommeDonner=" + getSommeDonner() +
            ", avoir=" + getAvoir() +
            "}";
    }
}
