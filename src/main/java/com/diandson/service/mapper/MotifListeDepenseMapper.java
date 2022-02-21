package com.diandson.service.mapper;

import com.diandson.domain.MotifListeDepense;
import com.diandson.service.dto.MotifListeDepenseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MotifListeDepense} and its DTO {@link MotifListeDepenseDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MotifListeDepenseMapper extends EntityMapper<MotifListeDepenseDTO, MotifListeDepense> {}
