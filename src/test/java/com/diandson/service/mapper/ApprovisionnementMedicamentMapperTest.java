package com.diandson.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApprovisionnementMedicamentMapperTest {

    private ApprovisionnementMedicamentMapper approvisionnementMedicamentMapper;

    @BeforeEach
    public void setUp() {
        approvisionnementMedicamentMapper = new ApprovisionnementMedicamentMapperImpl();
    }
}
