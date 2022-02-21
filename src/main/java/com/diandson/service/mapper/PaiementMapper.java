package com.diandson.service.mapper;

import com.diandson.domain.Paiement;
import com.diandson.service.dto.PaiementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paiement} and its DTO {@link PaiementDTO}.
 */
@Mapper(componentModel = "spring", uses = { VersementMapper.class, PersonneMapper.class, VenteMapper.class })
public interface PaiementMapper extends EntityMapper<PaiementDTO, Paiement> {
    @Mapping(target = "versement", source = "versement", qualifiedByName = "id")
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "id")
    @Mapping(target = "vente", source = "vente", qualifiedByName = "id")
    PaiementDTO toDto(Paiement s);
}
