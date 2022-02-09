package com.diandson.service.mapper;

import com.diandson.domain.Pack;
import com.diandson.service.dto.PackDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pack} and its DTO {@link PackDTO}.
 */
@Mapper(componentModel = "spring", uses = { StructureMapper.class, TypePackMapper.class })
public interface PackMapper extends EntityMapper<PackDTO, Pack> {
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "id")
    @Mapping(target = "type", source = "type", qualifiedByName = "id")
    PackDTO toDto(Pack s);
}
