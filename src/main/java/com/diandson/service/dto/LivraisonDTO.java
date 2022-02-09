package com.diandson.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.Livraison} entity.
 */
public class LivraisonDTO implements Serializable {

    private Long id;

    private String numero;

    private ZonedDateTime dateLivraison;

    private PersonneDTO operateur;

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

    public ZonedDateTime getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(ZonedDateTime dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public PersonneDTO getOperateur() {
        return operateur;
    }

    public void setOperateur(PersonneDTO operateur) {
        this.operateur = operateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LivraisonDTO)) {
            return false;
        }

        LivraisonDTO livraisonDTO = (LivraisonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, livraisonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LivraisonDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dateLivraison='" + getDateLivraison() + "'" +
            ", operateur=" + getOperateur() +
            "}";
    }
}
