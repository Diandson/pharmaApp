package com.diandson.service.mapper;

import com.diandson.domain.Notification;
import com.diandson.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonneMapper.class })
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "id")
    NotificationDTO toDto(Notification s);
}
