package com.diandson.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventaireMedicamentMapperTest {

    private InventaireMedicamentMapper inventaireMedicamentMapper;

    @BeforeEach
    public void setUp() {
        inventaireMedicamentMapper = new InventaireMedicamentMapperImpl();
    }
}
