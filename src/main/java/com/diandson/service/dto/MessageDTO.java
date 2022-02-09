package com.diandson.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.diandson.domain.Message} entity.
 */
public class MessageDTO implements Serializable {

    private Long id;

    private String message;

    private ZonedDateTime dateMessage;

    private String to;

    private Boolean vue;

    private PersonneDTO from;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getDateMessage() {
        return dateMessage;
    }

    public void setDateMessage(ZonedDateTime dateMessage) {
        this.dateMessage = dateMessage;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Boolean getVue() {
        return vue;
    }

    public void setVue(Boolean vue) {
        this.vue = vue;
    }

    public PersonneDTO getFrom() {
        return from;
    }

    public void setFrom(PersonneDTO from) {
        this.from = from;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageDTO)) {
            return false;
        }

        MessageDTO messageDTO = (MessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, messageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageDTO{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", dateMessage='" + getDateMessage() + "'" +
            ", to='" + getTo() + "'" +
            ", vue='" + getVue() + "'" +
            ", from=" + getFrom() +
            "}";
    }
}
