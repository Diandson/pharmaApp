package com.diandson.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.Commande} entity.
 */
public class CommandeDTO implements Serializable {

    private Long id;

    private String numero;

    private ZonedDateTime dateCommande;

    private LivraisonDTO livraison;

    private FournisseurDTO fournisseur;

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

    public ZonedDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(ZonedDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public LivraisonDTO getLivraison() {
        return livraison;
    }

    public void setLivraison(LivraisonDTO livraison) {
        this.livraison = livraison;
    }

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
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
        if (!(o instanceof CommandeDTO)) {
            return false;
        }

        CommandeDTO commandeDTO = (CommandeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commandeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandeDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", dateCommande='" + getDateCommande() + "'" +
            ", livraison=" + getLivraison() +
            ", fournisseur=" + getFournisseur() +
            ", operateur=" + getOperateur() +
            "}";
    }
}
