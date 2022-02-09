package com.diandson.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.Client} entity.
 */
public class ClientDTO implements Serializable {

    private Long id;

    private String nom;

    private String prenom;

    private String telephone;

    private String numeroAssure;

    private AssuranceDTO assurance;

    private PersonneDTO operateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNumeroAssure() {
        return numeroAssure;
    }

    public void setNumeroAssure(String numeroAssure) {
        this.numeroAssure = numeroAssure;
    }

    public AssuranceDTO getAssurance() {
        return assurance;
    }

    public void setAssurance(AssuranceDTO assurance) {
        this.assurance = assurance;
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
        if (!(o instanceof ClientDTO)) {
            return false;
        }

        ClientDTO clientDTO = (ClientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", numeroAssure='" + getNumeroAssure() + "'" +
            ", assurance=" + getAssurance() +
            ", operateur=" + getOperateur() +
            "}";
    }
}
