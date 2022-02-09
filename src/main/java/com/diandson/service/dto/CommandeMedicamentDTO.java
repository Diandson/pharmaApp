package com.diandson.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.CommandeMedicament} entity.
 */
public class CommandeMedicamentDTO implements Serializable {

    private Long id;

    private Long quantite;

    private MedicamentDTO medicament;

    private CommandeDTO commande;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantite() {
        return quantite;
    }

    public void setQuantite(Long quantite) {
        this.quantite = quantite;
    }

    public MedicamentDTO getMedicament() {
        return medicament;
    }

    public void setMedicament(MedicamentDTO medicament) {
        this.medicament = medicament;
    }

    public CommandeDTO getCommande() {
        return commande;
    }

    public void setCommande(CommandeDTO commande) {
        this.commande = commande;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandeMedicamentDTO)) {
            return false;
        }

        CommandeMedicamentDTO commandeMedicamentDTO = (CommandeMedicamentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commandeMedicamentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandeMedicamentDTO{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", medicament=" + getMedicament() +
            ", commande=" + getCommande() +
            "}";
    }
}
