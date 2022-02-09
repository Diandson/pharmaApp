package com.diandson.service.mapper;

import com.diandson.domain.Inventaire;
import com.diandson.service.dto.InventaireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inventaire} and its DTO {@link InventaireDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonneMapper.class })
public interface InventaireMapper extends EntityMapper<InventaireDTO, Inventaire> {
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "id")
    InventaireDTO toDto(Inventaire s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InventaireDTO toDtoId(Inventaire inventaire);
}
