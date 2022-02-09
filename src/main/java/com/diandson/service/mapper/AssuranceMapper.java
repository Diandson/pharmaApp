package com.diandson.service.mapper;

import com.diandson.domain.Assurance;
import com.diandson.service.dto.AssuranceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Assurance} and its DTO {@link AssuranceDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonneMapper.class })
public interface AssuranceMapper extends EntityMapper<AssuranceDTO, Assurance> {
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "id")
    AssuranceDTO toDto(Assurance s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssuranceDTO toDtoId(Assurance assurance);
}
