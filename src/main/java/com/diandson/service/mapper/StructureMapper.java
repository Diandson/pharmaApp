package com.diandson.service.mapper;

import com.diandson.domain.Structure;
import com.diandson.service.dto.StructureDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Structure} and its DTO {@link StructureDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StructureMapper extends EntityMapper<StructureDTO, Structure> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StructureDTO toDtoId(Structure structure);
}
