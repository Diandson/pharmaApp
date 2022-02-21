package com.diandson.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LieuVersementMapperTest {

    private LieuVersementMapper lieuVersementMapper;

    @BeforeEach
    public void setUp() {
        lieuVersementMapper = new LieuVersementMapperImpl();
    }
}
