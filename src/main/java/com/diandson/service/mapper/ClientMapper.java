package com.diandson.service.mapper;

import com.diandson.domain.Client;
import com.diandson.service.dto.ClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring", uses = { AssuranceMapper.class, PersonneMapper.class })
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {
    @Mapping(target = "assurance", source = "assurance", qualifiedByName = "id")
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "id")
    ClientDTO toDto(Client s);
}
