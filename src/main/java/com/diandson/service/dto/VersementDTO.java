package com.diandson.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.Versement} entity.
 */
public class VersementDTO implements Serializable {

    private Long id;

    private String numero;

    private String commentaire;

    private Long montant;

    private Long resteAVerser;

    private String lieuVersement;

    private String referenceVersement;

    private String identiteReceveur;

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

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Long getMontant() {
        return montant;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public Long getResteAVerser() {
        return resteAVerser;
    }

    public void setResteAVerser(Long resteAVerser) {
        this.resteAVerser = resteAVerser;
    }

    public String getLieuVersement() {
        return lieuVersement;
    }

    public void setLieuVersement(String lieuVersement) {
        this.lieuVersement = lieuVersement;
    }

    public String getReferenceVersement() {
        return referenceVersement;
    }

    public void setReferenceVersement(String referenceVersement) {
        this.referenceVersement = referenceVersement;
    }

    public String getIdentiteReceveur() {
        return identiteReceveur;
    }

    public void setIdentiteReceveur(String identiteReceveur) {
        this.identiteReceveur = identiteReceveur;
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
        if (!(o instanceof VersementDTO)) {
            return false;
        }

        VersementDTO versementDTO = (VersementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, versementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VersementDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", montant=" + getMontant() +
            ", resteAVerser=" + getResteAVerser() +
            ", lieuVersement='" + getLieuVersement() + "'" +
            ", referenceVersement='" + getReferenceVersement() + "'" +
            ", identiteReceveur='" + getIdentiteReceveur() + "'" +
            ", operateur=" + getOperateur() +
            "}";
    }
}
