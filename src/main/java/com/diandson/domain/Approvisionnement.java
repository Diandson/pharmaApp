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
 * A Approvisionnement.
 */
@Entity
@Table(name = "approvisionnement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Approvisionnement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero")
    private String numero;

    @Column(name = "agence_app")
    private String agenceApp;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "date_commande")
    private ZonedDateTime dateCommande;

    @OneToMany(mappedBy = "approvionnement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "medicament", "approvionnement" }, allowSetters = true)
    private Set<ApprovisionnementMedicament> approvisionnementMedicaments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Approvisionnement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public Approvisionnement numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAgenceApp() {
        return this.agenceApp;
    }

    public Approvisionnement agenceApp(String agenceApp) {
        this.setAgenceApp(agenceApp);
        return this;
    }

    public void setAgenceApp(String agenceApp) {
        this.agenceApp = agenceApp;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Approvisionnement commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public ZonedDateTime getDateCommande() {
        return this.dateCommande;
    }

    public Approvisionnement dateCommande(ZonedDateTime dateCommande) {
        this.setDateCommande(dateCommande);
        return this;
    }

    public void setDateCommande(ZonedDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Set<ApprovisionnementMedicament> getApprovisionnementMedicaments() {
        return this.approvisionnementMedicaments;
    }

    public void setApprovisionnementMedicaments(Set<ApprovisionnementMedicament> approvisionnementMedicaments) {
        if (this.approvisionnementMedicaments != null) {
            this.approvisionnementMedicaments.forEach(i -> i.setApprovionnement(null));
        }
        if (approvisionnementMedicaments != null) {
            approvisionnementMedicaments.forEach(i -> i.setApprovionnement(this));
        }
        this.approvisionnementMedicaments = approvisionnementMedicaments;
    }

    public Approvisionnement approvisionnementMedicaments(Set<ApprovisionnementMedicament> approvisionnementMedicaments) {
        this.setApprovisionnementMedicaments(approvisionnementMedicaments);
        return this;
    }

    public Approvisionnement addApprovisionnementMedicament(ApprovisionnementMedicament approvisionnementMedicament) {
        this.approvisionnementMedicaments.add(approvisionnementMedicament);
        approvisionnementMedicament.setApprovionnement(this);
        return this;
    }

    public Approvisionnement removeApprovisionnementMedicament(ApprovisionnementMedicament approvisionnementMedicament) {
        this.approvisionnementMedicaments.remove(approvisionnementMedicament);
        approvisionnementMedicament.setApprovionnement(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Approvisionnement)) {
            return false;
        }
        return id != null && id.equals(((Approvisionnement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Approvisionnement{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", agenceApp='" + getAgenceApp() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", dateCommande='" + getDateCommande() + "'" +
            "}";
    }
}
