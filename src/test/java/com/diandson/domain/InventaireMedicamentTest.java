package com.diandson.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventaireMedicamentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InventaireMedicament.class);
        InventaireMedicament inventaireMedicament1 = new InventaireMedicament();
        inventaireMedicament1.setId(1L);
        InventaireMedicament inventaireMedicament2 = new InventaireMedicament();
        inventaireMedicament2.setId(inventaireMedicament1.getId());
        assertThat(inventaireMedicament1).isEqualTo(inventaireMedicament2);
        inventaireMedicament2.setId(2L);
        assertThat(inventaireMedicament1).isNotEqualTo(inventaireMedicament2);
        inventaireMedicament1.setId(null);
        assertThat(inventaireMedicament1).isNotEqualTo(inventaireMedicament2);
    }
}
