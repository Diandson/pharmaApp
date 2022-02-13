package com.diandson.service.dto;

import com.diandson.domain.enumeration.TypeStructure;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.diandson.domain.Structure} entity.
 */
public class StructureDTO implements Serializable {

    private Long id;

    private String denomination;

    private String ifu;

    private String rccm;

    private String codePostal;

    private String localisation;

    private String contact;

    private String regime;

    private String division;

    private String email;

    @Lob
    private byte[] logo;

    private String logoContentType;

    @Lob
    private byte[] cachet;

    private String cachetContentType;

    @Lob
    private byte[] signature;

    private String signatureContentType;
    private ZonedDateTime dateConfig;

    private String pdg;

    private TypeStructure type;

    private PersonneDTO personne;

    private AdminUserDTO userDTO;

    private PackDTO packDTO;

    private String mere;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getIfu() {
        return ifu;
    }

    public void setIfu(String ifu) {
        this.ifu = ifu;
    }

    public String getRccm() {
        return rccm;
    }

    public void setRccm(String rccm) {
        this.rccm = rccm;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRegime() {
        return regime;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public byte[] getCachet() {
        return cachet;
    }

    public void setCachet(byte[] cachet) {
        this.cachet = cachet;
    }

    public String getCachetContentType() {
        return cachetContentType;
    }

    public void setCachetContentType(String cachetContentType) {
        this.cachetContentType = cachetContentType;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getSignatureContentType() {
        return signatureContentType;
    }

    public void setSignatureContentType(String signatureContentType) {
        this.signatureContentType = signatureContentType;
    }

    public ZonedDateTime getDateConfig() {
        return dateConfig;
    }

    public void setDateConfig(ZonedDateTime dateConfig) {
        this.dateConfig = dateConfig;
    }

    public String getPdg() {
        return pdg;
    }

    public void setPdg(String pdg) {
        this.pdg = pdg;
    }

    public TypeStructure getType() {
        return type;
    }

    public void setType(TypeStructure type) {
        this.type = type;
    }

    public PersonneDTO getPersonne() {
        return personne;
    }

    public void setPersonne(PersonneDTO personne) {
        this.personne = personne;
    }

    public AdminUserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(AdminUserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public PackDTO getPackDTO() {
        return packDTO;
    }

    public void setPackDTO(PackDTO packDTO) {
        this.packDTO = packDTO;
    }

    public String getMere() {
        return mere;
    }

    public void setMere(String mere) {
        this.mere = mere;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StructureDTO)) {
            return false;
        }

        StructureDTO structureDTO = (StructureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, structureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StructureDTO{" +
            "id=" + getId() +
            ", denomination='" + getDenomination() + "'" +
            ", ifu='" + getIfu() + "'" +
            ", rccm='" + getRccm() + "'" +
            ", codePostal='" + getCodePostal() + "'" +
            ", localisation='" + getLocalisation() + "'" +
            ", contact='" + getContact() + "'" +
            ", regime='" + getRegime() + "'" +
            ", division='" + getDivision() + "'" +
            ", email='" + getEmail() + "'" +
            ", logo='" + getLogo() + "'" +
            ", cachet='" + getCachet() + "'" +
            ", signature='" + getSignature() + "'" +
            ", dateConfig='" + getDateConfig() + "'" +
            ", pdg='" + getPdg() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
