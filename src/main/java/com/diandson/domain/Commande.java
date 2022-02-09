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
 * A Commande.
 */
@Entity
@Table(name = "commande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "date_commande")
    private ZonedDateTime dateCommande;

    @JsonIgnoreProperties(value = { "commande", "operateur" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Livraison livraison;

    @JsonIgnoreProperties(value = { "operateur" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "commande")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "medicament", "commande" }, allowSetters = true)
    private Set<CommandeMedicament> commandeMedicaments = new HashSet<>();

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

    public Commande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Commande numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public ZonedDateTime getDateCommande() {
        return this.dateCommande;
    }

    public Commande dateCommande(ZonedDateTime dateCommande) {
        this.setDateCommande(dateCommande);
        return this;
    }

    public void setDateCommande(ZonedDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Livraison getLivraison() {
        return this.livraison;
    }

    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
    }

    public Commande livraison(Livraison livraison) {
        this.setLivraison(livraison);
        return this;
    }

    public Fournisseur getFournisseur() {
        return this.fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Commande fournisseur(Fournisseur fournisseur) {
        this.setFournisseur(fournisseur);
        return this;
    }

    public Set<CommandeMedicament> getCommandeMedicaments() {
        return this.commandeMedicaments;
    }

    public void setCommandeMedicaments(Set<CommandeMedicament> commandeMedicaments) {
        if (this.commandeMedicaments != null) {
            this.commandeMedicaments.forEach(i -> i.setCommande(null));
        }
        if (commandeMedicaments != null) {
            commandeMedicaments.forEach(i -> i.setCommande(this));
        }
        this.commandeMedicaments = commandeMedicaments;
    }

    public Commande commandeMedicaments(Set<CommandeMedicament> commandeMedicaments) {
        this.setCommandeMedicaments(commandeMedicaments);
        return this;
    }

    public Commande addCommandeMedicament(CommandeMedicament commandeMedicament) {
        this.commandeMedicaments.add(commandeMedicament);
        commandeMedicament.setCommande(this);
        return this;
    }

    public Commande removeCommandeMedicament(CommandeMedicament commandeMedicament) {
        this.commandeMedicaments.remove(commandeMedicament);
        commandeMedicament.setCommande(null);
        return this;
    }

    public Personne getOperateur() {
        return this.operateur;
    }

    public void setOperateur(Personne personne) {
        this.operateur = personne;
    }

    public Commande operateur(Personne personne) {
        this.setOperateur(personne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commande)) {
            return false;
        }
        return id != null && id.equals(((Commande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commande{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dateCommande='" + getDateCommande() + "'" +
            "}";
    }
}
