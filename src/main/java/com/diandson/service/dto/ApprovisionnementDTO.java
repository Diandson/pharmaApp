package com.diandson.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.Approvisionnement} entity.
 */
public class ApprovisionnementDTO implements Serializable {

    private Long id;

    private String numero;

    private String agenceApp;

    private String commentaire;

    private ZonedDateTime dateCommande;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getAgenceApp() {
        return agenceApp;
    }

    public void setAgenceApp(String agenceApp) {
        this.agenceApp = agenceApp;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public ZonedDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(ZonedDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovisionnementDTO)) {
            return false;
        }

        ApprovisionnementDTO approvisionnementDTO = (ApprovisionnementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, approvisionnementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovisionnementDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", agenceApp='" + getAgenceApp() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", dateCommande='" + getDateCommande() + "'" +
            "}";
    }
}
