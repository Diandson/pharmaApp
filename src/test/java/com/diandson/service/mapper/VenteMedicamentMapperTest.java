package com.diandson.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VenteMedicamentMapperTest {

    private VenteMedicamentMapper venteMedicamentMapper;

    @BeforeEach
    public void setUp() {
        venteMedicamentMapper = new VenteMedicamentMapperImpl();
    }
}
