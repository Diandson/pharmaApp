package com.diandson.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventaireMapperTest {

    private InventaireMapper inventaireMapper;

    @BeforeEach
    public void setUp() {
        inventaireMapper = new InventaireMapperImpl();
    }
}
