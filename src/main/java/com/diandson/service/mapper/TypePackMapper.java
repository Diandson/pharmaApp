package com.diandson.service.mapper;

import com.diandson.domain.TypePack;
import com.diandson.service.dto.TypePackDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypePack} and its DTO {@link TypePackDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypePackMapper extends EntityMapper<TypePackDTO, TypePack> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypePackDTO toDtoId(TypePack typePack);
}
