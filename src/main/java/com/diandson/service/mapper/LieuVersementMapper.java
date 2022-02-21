package com.diandson.service.mapper;

import com.diandson.domain.LieuVersement;
import com.diandson.service.dto.LieuVersementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LieuVersement} and its DTO {@link LieuVersementDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LieuVersementMapper extends EntityMapper<LieuVersementDTO, LieuVersement> {}
