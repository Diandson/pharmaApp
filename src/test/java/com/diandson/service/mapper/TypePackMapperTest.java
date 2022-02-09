package com.diandson.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypePackMapperTest {

    private TypePackMapper typePackMapper;

    @BeforeEach
    public void setUp() {
        typePackMapper = new TypePackMapperImpl();
    }
}
