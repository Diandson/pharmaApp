package com.diandson.service.mapper;

import com.diandson.domain.Message;
import com.diandson.service.dto.MessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonneMapper.class })
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "from", source = "from", qualifiedByName = "id")
    MessageDTO toDto(Message s);
}
