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
 * A Versement.
 */
@Entity
@Table(name = "versement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Versement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "montant")
    private Long montant;

    @Column(name = "reste_a_verser")
    private Long resteAVerser;

    @Column(name = "lieu_versement")
    private String lieuVersement;

    @Column(name = "reference_versement")
    private String referenceVersement;

    @Column(name = "identite_receveur")
    private String identiteReceveur;

    @Column(name = "date_versement")
    private ZonedDateTime dateVersement;

    @OneToMany(mappedBy = "versement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "versement", "operateur", "vente" }, allowSetters = true)
    private Set<Paiement> paiements = new HashSet<>();

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

    public Versement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Versement numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Versement commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Long getMontant() {
        return this.montant;
    }

    public Versement montant(Long montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public Long getResteAVerser() {
        return this.resteAVerser;
    }

    public Versement resteAVerser(Long resteAVerser) {
        this.setResteAVerser(resteAVerser);
        return this;
    }

    public void setResteAVerser(Long resteAVerser) {
        this.resteAVerser = resteAVerser;
    }

    public String getLieuVersement() {
        return this.lieuVersement;
    }

    public Versement lieuVersement(String lieuVersement) {
        this.setLieuVersement(lieuVersement);
        return this;
    }

    public void setLieuVersement(String lieuVersement) {
        this.lieuVersement = lieuVersement;
    }

    public String getReferenceVersement() {
        return this.referenceVersement;
    }

    public Versement referenceVersement(String referenceVersement) {
        this.setReferenceVersement(referenceVersement);
        return this;
    }

    public void setReferenceVersement(String referenceVersement) {
        this.referenceVersement = referenceVersement;
    }

    public String getIdentiteReceveur() {
        return this.identiteReceveur;
    }

    public Versement identiteReceveur(String identiteReceveur) {
        this.setIdentiteReceveur(identiteReceveur);
        return this;
    }

    public void setIdentiteReceveur(String identiteReceveur) {
        this.identiteReceveur = identiteReceveur;
    }

    public ZonedDateTime getDateVersement() {
        return this.dateVersement;
    }

    public Versement dateVersement(ZonedDateTime dateVersement) {
        this.setDateVersement(dateVersement);
        return this;
    }

    public void setDateVersement(ZonedDateTime dateVersement) {
        this.dateVersement = dateVersement;
    }

    public Set<Paiement> getPaiements() {
        return this.paiements;
    }

    public void setPaiements(Set<Paiement> paiements) {
        if (this.paiements != null) {
            this.paiements.forEach(i -> i.setVersement(null));
        }
        if (paiements != null) {
            paiements.forEach(i -> i.setVersement(this));
        }
        this.paiements = paiements;
    }

    public Versement paiements(Set<Paiement> paiements) {
        this.setPaiements(paiements);
        return this;
    }

    public Versement addPaiement(Paiement paiement) {
        this.paiements.add(paiement);
        paiement.setVersement(this);
        return this;
    }

    public Versement removePaiement(Paiement paiement) {
        this.paiements.remove(paiement);
        paiement.setVersement(null);
        return this;
    }

    public Personne getOperateur() {
        return this.operateur;
    }

    public void setOperateur(Personne personne) {
        this.operateur = personne;
    }

    public Versement operateur(Personne personne) {
        this.setOperateur(personne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Versement)) {
            return false;
        }
        return id != null && id.equals(((Versement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Versement{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", montant=" + getMontant() +
            ", resteAVerser=" + getResteAVerser() +
            ", lieuVersement='" + getLieuVersement() + "'" +
            ", referenceVersement='" + getReferenceVersement() + "'" +
            ", identiteReceveur='" + getIdentiteReceveur() + "'" +
            ", dateVersement='" + getDateVersement() + "'" +
            "}";
    }
}
