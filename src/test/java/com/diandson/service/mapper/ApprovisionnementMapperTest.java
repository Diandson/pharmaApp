package com.diandson.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApprovisionnementMapperTest {

    private ApprovisionnementMapper approvisionnementMapper;

    @BeforeEach
    public void setUp() {
        approvisionnementMapper = new ApprovisionnementMapperImpl();
    }
}
