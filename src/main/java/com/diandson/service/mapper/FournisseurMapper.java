package com.diandson.service.mapper;

import com.diandson.domain.Fournisseur;
import com.diandson.service.dto.FournisseurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fournisseur} and its DTO {@link FournisseurDTO}.
 */
@Mapper(componentModel = "spring", uses = { PersonneMapper.class })
public interface FournisseurMapper extends EntityMapper<FournisseurDTO, Fournisseur> {
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "id")
    FournisseurDTO toDto(Fournisseur s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FournisseurDTO toDtoId(Fournisseur fournisseur);
}
