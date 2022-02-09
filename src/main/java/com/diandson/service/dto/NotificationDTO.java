package com.diandson.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.Notification} entity.
 */
public class NotificationDTO implements Serializable {

    private Long id;

    private String titre;

    private String content;

    private ZonedDateTime dateNotif;

    private Boolean vue;

    private PersonneDTO operateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getDateNotif() {
        return dateNotif;
    }

    public void setDateNotif(ZonedDateTime dateNotif) {
        this.dateNotif = dateNotif;
    }

    public Boolean getVue() {
        return vue;
    }

    public void setVue(Boolean vue) {
        this.vue = vue;
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
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", content='" + getContent() + "'" +
            ", dateNotif='" + getDateNotif() + "'" +
            ", vue='" + getVue() + "'" +
            ", operateur=" + getOperateur() +
            "}";
    }
}
