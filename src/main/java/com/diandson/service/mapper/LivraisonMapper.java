package com.diandson.service.mapper;

import com.diandson.domain.Livraison;
import com.diandson.service.dto.LivraisonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Livraison} and its DTO {@link LivraisonDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonneMapper.class })
public interface LivraisonMapper extends EntityMapper<LivraisonDTO, Livraison> {
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "id")
    LivraisonDTO toDto(Livraison s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LivraisonDTO toDtoId(Livraison livraison);
}
