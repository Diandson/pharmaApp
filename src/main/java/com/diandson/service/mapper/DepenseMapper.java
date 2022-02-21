package com.diandson.service.mapper;

import com.diandson.domain.Depense;
import com.diandson.service.dto.DepenseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Depense} and its DTO {@link DepenseDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DepenseMapper extends EntityMapper<DepenseDTO, Depense> {}
