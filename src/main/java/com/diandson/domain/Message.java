package com.diandson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "date_message")
    private ZonedDateTime dateMessage;

    @Column(name = "jhi_to")
    private String to;

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
    private Personne from;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Message id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return this.message;
    }

    public Message message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getDateMessage() {
        return this.dateMessage;
    }

    public Message dateMessage(ZonedDateTime dateMessage) {
        this.setDateMessage(dateMessage);
        return this;
    }

    public void setDateMessage(ZonedDateTime dateMessage) {
        this.dateMessage = dateMessage;
    }

    public String getTo() {
        return this.to;
    }

    public Message to(String to) {
        this.setTo(to);
        return this;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Boolean getVue() {
        return this.vue;
    }

    public Message vue(Boolean vue) {
        this.setVue(vue);
        return this;
    }

    public void setVue(Boolean vue) {
        this.vue = vue;
    }

    public Personne getFrom() {
        return this.from;
    }

    public void setFrom(Personne personne) {
        this.from = personne;
    }

    public Message from(Personne personne) {
        this.setFrom(personne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        return id != null && id.equals(((Message) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", dateMessage='" + getDateMessage() + "'" +
            ", to='" + getTo() + "'" +
            ", vue='" + getVue() + "'" +
            "}";
    }
}
