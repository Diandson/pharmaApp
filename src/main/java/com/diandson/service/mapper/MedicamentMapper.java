package com.diandson.service.mapper;

import com.diandson.domain.Medicament;
import com.diandson.service.dto.MedicamentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medicament} and its DTO {@link MedicamentDTO}.
 */
@Mapper(componentModel = "spring", uses = { StructureMapper.class })
public interface MedicamentMapper extends EntityMapper<MedicamentDTO, Medicament> {
    @Mapping(target = "structure", source = "structure", qualifiedByName = "id")
    MedicamentDTO toDto(Medicament s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicamentDTO toDtoId(Medicament medicament);
}
