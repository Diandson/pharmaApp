package com.diandson.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepenseMapperTest {

    private DepenseMapper depenseMapper;

    @BeforeEach
    public void setUp() {
        depenseMapper = new DepenseMapperImpl();
    }
}
