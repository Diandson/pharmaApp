package com.diandson.service.mapper;

import com.diandson.domain.Vente;
import com.diandson.service.dto.VenteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vente} and its DTO {@link VenteDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonneMapper.class })
public interface VenteMapper extends EntityMapper<VenteDTO, Vente> {
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "id")
    VenteDTO toDto(Vente s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VenteDTO toDtoId(Vente vente);
}
