package com.diandson.service.mapper;

import com.diandson.domain.Commande;
import com.diandson.service.dto.CommandeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commande} and its DTO {@link CommandeDTO}.
 */
@Mapper(componentModel = "spring", uses = { LivraisonMapper.class, FournisseurMapper.class, PersonneMapper.class })
public interface CommandeMapper extends EntityMapper<CommandeDTO, Commande> {
    @Mapping(target = "livraison", source = "livraison", qualifiedByName = "id")
    @Mapping(target = "fournisseur", source = "fournisseur", qualifiedByName = "id")
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "id")
    CommandeDTO toDto(Commande s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommandeDTO toDtoId(Commande commande);
}
