package com.diandson.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.diandson.domain.Personne} entity.
 */
public class PersonneDTO implements Serializable {

    private Long id;

    private String nom;

    private String prenom;

    private LocalDate dataNaissance;

    private String lieuNaissance;

    private String numeroDoc;

    @Lob
    private byte[] profil;

    private String profilContentType;
    private String telephone;

    private UserDTO user;

    private StructureDTO structure;

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

    public LocalDate getDataNaissance() {
        return dataNaissance;
    }

    public void setDataNaissance(LocalDate dataNaissance) {
        this.dataNaissance = dataNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getNumeroDoc() {
        return numeroDoc;
    }

    public void setNumeroDoc(String numeroDoc) {
        this.numeroDoc = numeroDoc;
    }

    public byte[] getProfil() {
        return profil;
    }

    public void setProfil(byte[] profil) {
        this.profil = profil;
    }

    public String getProfilContentType() {
        return profilContentType;
    }

    public void setProfilContentType(String profilContentType) {
        this.profilContentType = profilContentType;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public StructureDTO getStructure() {
        return structure;
    }

    public void setStructure(StructureDTO structure) {
        this.structure = structure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonneDTO)) {
            return false;
        }

        PersonneDTO personneDTO = (PersonneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonneDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", dataNaissance='" + getDataNaissance() + "'" +
            ", lieuNaissance='" + getLieuNaissance() + "'" +
            ", numeroDoc='" + getNumeroDoc() + "'" +
            ", profil='" + getProfil() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", user=" + getUser() +
            ", structure=" + getStructure() +
            "}";
    }
}
