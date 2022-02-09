package com.diandson.service.mapper;

import com.diandson.domain.InventaireMedicament;
import com.diandson.service.dto.InventaireMedicamentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InventaireMedicament} and its DTO {@link InventaireMedicamentDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicamentMapper.class, InventaireMapper.class })
public interface InventaireMedicamentMapper extends EntityMapper<InventaireMedicamentDTO, InventaireMedicament> {
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "id")
    @Mapping(target = "inventaire", source = "inventaire", qualifiedByName = "id")
    InventaireMedicamentDTO toDto(InventaireMedicament s);
}
