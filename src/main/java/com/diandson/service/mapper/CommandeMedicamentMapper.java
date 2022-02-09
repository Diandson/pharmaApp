package com.diandson.service.mapper;

import com.diandson.domain.CommandeMedicament;
import com.diandson.service.dto.CommandeMedicamentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommandeMedicament} and its DTO {@link CommandeMedicamentDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicamentMapper.class, CommandeMapper.class })
public interface CommandeMedicamentMapper extends EntityMapper<CommandeMedicamentDTO, CommandeMedicament> {
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "id")
    @Mapping(target = "commande", source = "commande", qualifiedByName = "id")
    CommandeMedicamentDTO toDto(CommandeMedicament s);
}
