package com.diandson.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MotifListeDepenseMapperTest {

    private MotifListeDepenseMapper motifListeDepenseMapper;

    @BeforeEach
    public void setUp() {
        motifListeDepenseMapper = new MotifListeDepenseMapperImpl();
    }
}
