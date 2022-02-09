package com.diandson.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StructureMapperTest {

    private StructureMapper structureMapper;

    @BeforeEach
    public void setUp() {
        structureMapper = new StructureMapperImpl();
    }
}
