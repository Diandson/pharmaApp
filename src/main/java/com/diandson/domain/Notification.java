package com.diandson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "titre")
    private String titre;

    @Column(name = "content")
    private String content;

    @Column(name = "date_notif")
    private ZonedDateTime dateNotif;

    @Column(name = "vue")
    private Boolean vue;

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

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public Notification titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContent() {
        return this.content;
    }

    public Notification content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getDateNotif() {
        return this.dateNotif;
    }

    public Notification dateNotif(ZonedDateTime dateNotif) {
        this.setDateNotif(dateNotif);
        return this;
    }

    public void setDateNotif(ZonedDateTime dateNotif) {
        this.dateNotif = dateNotif;
    }

    public Boolean getVue() {
        return this.vue;
    }

    public Notification vue(Boolean vue) {
        this.setVue(vue);
        return this;
    }

    public void setVue(Boolean vue) {
        this.vue = vue;
    }

    public Personne getOperateur() {
        return this.operateur;
    }

    public void setOperateur(Personne personne) {
        this.operateur = personne;
    }

    public Notification operateur(Personne personne) {
        this.setOperateur(personne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", content='" + getContent() + "'" +
            ", dateNotif='" + getDateNotif() + "'" +
            ", vue='" + getVue() + "'" +
            "}";
    }
}
