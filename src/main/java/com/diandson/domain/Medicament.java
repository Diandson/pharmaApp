package com.diandson.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Medicament.
 */
@Entity
@Table(name = "medicament")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Medicament implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "denomination")
    private String denomination;

    @Column(name = "dci")
    private String dci;

    @Column(name = "forme")
    private String forme;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "classe")
    private String classe;

    @Size(max = 5000)
    @Column(name = "code_bare", length = 5000)
    private String codeBare;

    @Column(name = "prix_achat")
    private Long prixAchat;

    @Column(name = "prix_public")
    private Long prixPublic;

    @Column(name = "stock_alerte")
    private Long stockAlerte;

    @Column(name = "stock_securite")
    private Long stockSecurite;

    @Column(name = "stock_theorique")
    private Long stockTheorique;

    @Column(name = "date_fabrication")
    private LocalDate dateFabrication;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "packs", "medicaments", "personnes" }, allowSetters = true)
    private Structure structure;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medicament id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenomination() {
        return this.denomination;
    }

    public Medicament denomination(String denomination) {
        this.setDenomination(denomination);
        return this;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getDci() {
        return this.dci;
    }

    public Medicament dci(String dci) {
        this.setDci(dci);
        return this;
    }

    public void setDci(String dci) {
        this.dci = dci;
    }

    public String getForme() {
        return this.forme;
    }

    public Medicament forme(String forme) {
        this.setForme(forme);
        return this;
    }

    public void setForme(String forme) {
        this.forme = forme;
    }

    public String getDosage() {
        return this.dosage;
    }

    public Medicament dosage(String dosage) {
        this.setDosage(dosage);
        return this;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getClasse() {
        return this.classe;
    }

    public Medicament classe(String classe) {
        this.setClasse(classe);
        return this;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getCodeBare() {
        return this.codeBare;
    }

    public Medicament codeBare(String codeBare) {
        this.setCodeBare(codeBare);
        return this;
    }

    public void setCodeBare(String codeBare) {
        this.codeBare = codeBare;
    }

    public Long getPrixAchat() {
        return this.prixAchat;
    }

    public Medicament prixAchat(Long prixAchat) {
        this.setPrixAchat(prixAchat);
        return this;
    }

    public void setPrixAchat(Long prixAchat) {
        this.prixAchat = prixAchat;
    }

    public Long getPrixPublic() {
        return this.prixPublic;
    }

    public Medicament prixPublic(Long prixPublic) {
        this.setPrixPublic(prixPublic);
        return this;
    }

    public void setPrixPublic(Long prixPublic) {
        this.prixPublic = prixPublic;
    }

    public Long getStockAlerte() {
        return this.stockAlerte;
    }

    public Medicament stockAlerte(Long stockAlerte) {
        this.setStockAlerte(stockAlerte);
        return this;
    }

    public void setStockAlerte(Long stockAlerte) {
        this.stockAlerte = stockAlerte;
    }

    public Long getStockSecurite() {
        return this.stockSecurite;
    }

    public Medicament stockSecurite(Long stockSecurite) {
        this.setStockSecurite(stockSecurite);
        return this;
    }

    public void setStockSecurite(Long stockSecurite) {
        this.stockSecurite = stockSecurite;
    }

    public Long getStockTheorique() {
        return this.stockTheorique;
    }

    public Medicament stockTheorique(Long stockTheorique) {
        this.setStockTheorique(stockTheorique);
        return this;
    }

    public void setStockTheorique(Long stockTheorique) {
        this.stockTheorique = stockTheorique;
    }

    public LocalDate getDateFabrication() {
        return this.dateFabrication;
    }

    public Medicament dateFabrication(LocalDate dateFabrication) {
        this.setDateFabrication(dateFabrication);
        return this;
    }

    public void setDateFabrication(LocalDate dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    public LocalDate getDateExpiration() {
        return this.dateExpiration;
    }

    public Medicament dateExpiration(LocalDate dateExpiration) {
        this.setDateExpiration(dateExpiration);
        return this;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Medicament image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Medicament imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Structure getStructure() {
        return this.structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public Medicament structure(Structure structure) {
        this.setStructure(structure);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medicament)) {
            return false;
        }
        return id != null && id.equals(((Medicament) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medicament{" +
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
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
