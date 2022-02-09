package com.diandson.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.diandson.domain.Medicament} entity.
 */
public class MedicamentDTO implements Serializable {

    private Long id;

    private String denomination;

    private String dci;

    private String forme;

    private String dosage;

    private String classe;

    @Size(max = 5000)
    private String codeBare;

    private Long prixAchat;

    private Long prixPublic;

    private Long stockAlerte;

    private Long stockSecurite;

    private Long stockTheorique;

    private LocalDate dateFabrication;

    private LocalDate dateExpiration;

    @Lob
    private byte[] image;

    private String imageContentType;
    private StructureDTO structure;

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

    public String getDci() {
        return dci;
    }

    public void setDci(String dci) {
        this.dci = dci;
    }

    public String getForme() {
        return forme;
    }

    public void setForme(String forme) {
        this.forme = forme;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getCodeBare() {
        return codeBare;
    }

    public void setCodeBare(String codeBare) {
        this.codeBare = codeBare;
    }

    public Long getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(Long prixAchat) {
        this.prixAchat = prixAchat;
    }

    public Long getPrixPublic() {
        return prixPublic;
    }

    public void setPrixPublic(Long prixPublic) {
        this.prixPublic = prixPublic;
    }

    public Long getStockAlerte() {
        return stockAlerte;
    }

    public void setStockAlerte(Long stockAlerte) {
        this.stockAlerte = stockAlerte;
    }

    public Long getStockSecurite() {
        return stockSecurite;
    }

    public void setStockSecurite(Long stockSecurite) {
        this.stockSecurite = stockSecurite;
    }

    public Long getStockTheorique() {
        return stockTheorique;
    }

    public void setStockTheorique(Long stockTheorique) {
        this.stockTheorique = stockTheorique;
    }

    public LocalDate getDateFabrication() {
        return dateFabrication;
    }

    public void setDateFabrication(LocalDate dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
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
        if (!(o instanceof MedicamentDTO)) {
            return false;
        }

        MedicamentDTO medicamentDTO = (MedicamentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicamentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicamentDTO{" +
            "id=" + getId() +
            ", denomination='" + getDenomination() + "'" +
            ", dci='" + getDci() + "'" +
            ", forme='" + getForme() + "'" +
            ", dosage='" + getDosage() + "'" +
            ", classe='" + getClasse() + "'" +
            ", codeBare='" + getCodeBare() + "'" +
            ", prixAchat=" + getPrixAchat() +
            ", prixPublic=" + getPrixPublic() +
            ", stockAlerte=" + getStockAlerte() +
            ", stockSecurite=" + getStockSecurite() +
            ", stockTheorique=" + getStockTheorique() +
            ", dateFabrication='" + getDateFabrication() + "'" +
            ", dateExpiration='" + getDateExpiration() + "'" +
            ", image='" + getImage() + "'" +
            ", structure=" + getStructure() +
            "}";
    }
}
