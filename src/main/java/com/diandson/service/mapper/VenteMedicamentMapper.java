package com.diandson.service.mapper;

import com.diandson.domain.VenteMedicament;
import com.diandson.service.dto.VenteMedicamentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VenteMedicament} and its DTO {@link VenteMedicamentDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicamentMapper.class, VenteMapper.class })
public interface VenteMedicamentMapper extends EntityMapper<VenteMedicamentDTO, VenteMedicament> {
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "id")
    @Mapping(target = "vente", source = "vente", qualifiedByName = "id")
    VenteMedicamentDTO toDto(VenteMedicament s);
}
