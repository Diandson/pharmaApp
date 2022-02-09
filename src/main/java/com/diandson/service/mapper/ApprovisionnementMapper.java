package com.diandson.service.mapper;

import com.diandson.domain.Approvisionnement;
import com.diandson.service.dto.ApprovisionnementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Approvisionnement} and its DTO {@link ApprovisionnementDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApprovisionnementMapper extends EntityMapper<ApprovisionnementDTO, Approvisionnement> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApprovisionnementDTO toDtoId(Approvisionnement approvisionnement);
}
