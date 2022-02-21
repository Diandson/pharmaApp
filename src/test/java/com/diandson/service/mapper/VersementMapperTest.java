package com.diandson.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VersementMapperTest {

    private VersementMapper versementMapper;

    @BeforeEach
    public void setUp() {
        versementMapper = new VersementMapperImpl();
    }
}
