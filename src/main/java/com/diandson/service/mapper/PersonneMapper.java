package com.diandson.service.mapper;

import com.diandson.domain.Personne;
import com.diandson.service.dto.PersonneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Personne} and its DTO {@link PersonneDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, StructureMapper.class })
public interface PersonneMapper extends EntityMapper<PersonneDTO, Personne> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    @Mapping(target = "structure", source = "structure", qualifiedByName = "id")
    PersonneDTO toDto(Personne s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonneDTO toDtoId(Personne personne);
}
