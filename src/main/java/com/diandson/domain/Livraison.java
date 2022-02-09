package com.diandson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Livraison.
 */
@Entity
@Table(name = "livraison")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Livraison implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "date_livraison")
    private ZonedDateTime dateLivraison;

    @JsonIgnoreProperties(value = { "livraison", "fournisseur", "commandeMedicaments", "operateur" }, allowSetters = true)
    @OneToOne(mappedBy = "livraison")
    private Commande commande;

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

    public Livraison id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Livraison numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public ZonedDateTime getDateLivraison() {
        return this.dateLivraison;
    }

    public Livraison dateLivraison(ZonedDateTime dateLivraison) {
        this.setDateLivraison(dateLivraison);
        return this;
    }

    public void setDateLivraison(ZonedDateTime dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public Commande getCommande() {
        return this.commande;
    }

    public void setCommande(Commande commande) {
        if (this.commande != null) {
            this.commande.setLivraison(null);
        }
        if (commande != null) {
            commande.setLivraison(this);
        }
        this.commande = commande;
    }

    public Livraison commande(Commande commande) {
        this.setCommande(commande);
        return this;
    }

    public Personne getOperateur() {
        return this.operateur;
    }

    public void setOperateur(Personne personne) {
        this.operateur = personne;
    }

    public Livraison operateur(Personne personne) {
        this.setOperateur(personne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Livraison)) {
            return false;
        }
        return id != null && id.equals(((Livraison) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Livraison{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dateLivraison='" + getDateLivraison() + "'" +
            "}";
    }
}
