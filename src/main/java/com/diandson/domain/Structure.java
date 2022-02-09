package com.diandson.domain;

import com.diandson.domain.enumeration.TypeStructure;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Structure.
 */
@Entity
@Table(name = "structure")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Structure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "denomination")
    private String denomination;

    @Column(name = "ifu")
    private String ifu;

    @Column(name = "rccm")
    private String rccm;

    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "localisation")
    private String localisation;

    @Column(name = "contact")
    private String contact;

    @Column(name = "regime")
    private String regime;

    @Column(name = "division")
    private String division;

    @Column(name = "email")
    private String email;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @Lob
    @Column(name = "cachet")
    private byte[] cachet;

    @Column(name = "cachet_content_type")
    private String cachetContentType;

    @Lob
    @Column(name = "signature")
    private byte[] signature;

    @Column(name = "signature_content_type")
    private String signatureContentType;

    @Column(name = "date_config")
    private ZonedDateTime dateConfig;

    @Column(name = "pdg")
    private String pdg;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeStructure type;

    @OneToMany(mappedBy = "operateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "operateur", "type" }, allowSetters = true)
    private Set<Pack> packs = new HashSet<>();

    @OneToMany(mappedBy = "structure")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "structure" }, allowSetters = true)
    private Set<Medicament> medicaments = new HashSet<>();

    @OneToMany(mappedBy = "structure")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Personne> personnes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Structure id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenomination() {
        return this.denomination;
    }

    public Structure denomination(String denomination) {
        this.setDenomination(denomination);
        return this;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getIfu() {
        return this.ifu;
    }

    public Structure ifu(String ifu) {
        this.setIfu(ifu);
        return this;
    }

    public void setIfu(String ifu) {
        this.ifu = ifu;
    }

    public String getRccm() {
        return this.rccm;
    }

    public Structure rccm(String rccm) {
        this.setRccm(rccm);
        return this;
    }

    public void setRccm(String rccm) {
        this.rccm = rccm;
    }

    public String getCodePostal() {
        return this.codePostal;
    }

    public Structure codePostal(String codePostal) {
        this.setCodePostal(codePostal);
        return this;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getLocalisation() {
        return this.localisation;
    }

    public Structure localisation(String localisation) {
        this.setLocalisation(localisation);
        return this;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getContact() {
        return this.contact;
    }

    public Structure contact(String contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRegime() {
        return this.regime;
    }

    public Structure regime(String regime) {
        this.setRegime(regime);
        return this;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }

    public String getDivision() {
        return this.division;
    }

    public Structure division(String division) {
        this.setDivision(division);
        return this;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getEmail() {
        return this.email;
    }

    public Structure email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Structure logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Structure logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public byte[] getCachet() {
        return this.cachet;
    }

    public Structure cachet(byte[] cachet) {
        this.setCachet(cachet);
        return this;
    }

    public void setCachet(byte[] cachet) {
        this.cachet = cachet;
    }

    public String getCachetContentType() {
        return this.cachetContentType;
    }

    public Structure cachetContentType(String cachetContentType) {
        this.cachetContentType = cachetContentType;
        return this;
    }

    public void setCachetContentType(String cachetContentType) {
        this.cachetContentType = cachetContentType;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public Structure signature(byte[] signature) {
        this.setSignature(signature);
        return this;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getSignatureContentType() {
        return this.signatureContentType;
    }

    public Structure signatureContentType(String signatureContentType) {
        this.signatureContentType = signatureContentType;
        return this;
    }

    public void setSignatureContentType(String signatureContentType) {
        this.signatureContentType = signatureContentType;
    }

    public ZonedDateTime getDateConfig() {
        return this.dateConfig;
    }

    public Structure dateConfig(ZonedDateTime dateConfig) {
        this.setDateConfig(dateConfig);
        return this;
    }

    public void setDateConfig(ZonedDateTime dateConfig) {
        this.dateConfig = dateConfig;
    }

    public String getPdg() {
        return this.pdg;
    }

    public Structure pdg(String pdg) {
        this.setPdg(pdg);
        return this;
    }

    public void setPdg(String pdg) {
        this.pdg = pdg;
    }

    public TypeStructure getType() {
        return this.type;
    }

    public Structure type(TypeStructure type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeStructure type) {
        this.type = type;
    }

    public Set<Pack> getPacks() {
        return this.packs;
    }

    public void setPacks(Set<Pack> packs) {
        if (this.packs != null) {
            this.packs.forEach(i -> i.setOperateur(null));
        }
        if (packs != null) {
            packs.forEach(i -> i.setOperateur(this));
        }
        this.packs = packs;
    }

    public Structure packs(Set<Pack> packs) {
        this.setPacks(packs);
        return this;
    }

    public Structure addPack(Pack pack) {
        this.packs.add(pack);
        pack.setOperateur(this);
        return this;
    }

    public Structure removePack(Pack pack) {
        this.packs.remove(pack);
        pack.setOperateur(null);
        return this;
    }

    public Set<Medicament> getMedicaments() {
        return this.medicaments;
    }

    public void setMedicaments(Set<Medicament> medicaments) {
        if (this.medicaments != null) {
            this.medicaments.forEach(i -> i.setStructure(null));
        }
        if (medicaments != null) {
            medicaments.forEach(i -> i.setStructure(this));
        }
        this.medicaments = medicaments;
    }

    public Structure medicaments(Set<Medicament> medicaments) {
        this.setMedicaments(medicaments);
        return this;
    }

    public Structure addMedicament(Medicament medicament) {
        this.medicaments.add(medicament);
        medicament.setStructure(this);
        return this;
    }

    public Structure removeMedicament(Medicament medicament) {
        this.medicaments.remove(medicament);
        medicament.setStructure(null);
        return this;
    }

    public Set<Personne> getPersonnes() {
        return this.personnes;
    }

    public void setPersonnes(Set<Personne> personnes) {
        if (this.personnes != null) {
            this.personnes.forEach(i -> i.setStructure(null));
        }
        if (personnes != null) {
            personnes.forEach(i -> i.setStructure(this));
        }
        this.personnes = personnes;
    }

    public Structure personnes(Set<Personne> personnes) {
        this.setPersonnes(personnes);
        return this;
    }

    public Structure addPersonne(Personne personne) {
        this.personnes.add(personne);
        personne.setStructure(this);
        return this;
    }

    public Structure removePersonne(Personne personne) {
        this.personnes.remove(personne);
        personne.setStructure(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Structure)) {
            return false;
        }
        return id != null && id.equals(((Structure) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Structure{" +
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
            ", logoContentType='" + getLogoContentType() + "'" +
            ", cachet='" + getCachet() + "'" +
            ", cachetContentType='" + getCachetContentType() + "'" +
            ", signature='" + getSignature() + "'" +
            ", signatureContentType='" + getSignatureContentType() + "'" +
            ", dateConfig='" + getDateConfig() + "'" +
            ", pdg='" + getPdg() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
