package com.diandson.service.mapper;

import com.diandson.domain.Versement;
import com.diandson.service.dto.VersementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Versement} and its DTO {@link VersementDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonneMapper.class })
public interface VersementMapper extends EntityMapper<VersementDTO, Versement> {
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "id")
    VersementDTO toDto(Versement s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VersementDTO toDtoId(Versement versement);
}
