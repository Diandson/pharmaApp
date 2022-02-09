package com.diandson.service.mapper;

import com.diandson.domain.ApprovisionnementMedicament;
import com.diandson.service.dto.ApprovisionnementMedicamentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApprovisionnementMedicament} and its DTO {@link ApprovisionnementMedicamentDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicamentMapper.class, ApprovisionnementMapper.class })
public interface ApprovisionnementMedicamentMapper extends EntityMapper<ApprovisionnementMedicamentDTO, ApprovisionnementMedicament> {
    @Mapping(target = "medicament", source = "medicament", qualifiedByName = "id")
    @Mapping(target = "approvionnement", source = "approvionnement", qualifiedByName = "id")
    ApprovisionnementMedicamentDTO toDto(ApprovisionnementMedicament s);
}
