package com.diandson.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventaire.class);
        Inventaire inventaire1 = new Inventaire();
        inventaire1.setId(1L);
        Inventaire inventaire2 = new Inventaire();
        inventaire2.setId(inventaire1.getId());
        assertThat(inventaire1).isEqualTo(inventaire2);
        inventaire2.setId(2L);
        assertThat(inventaire1).isNotEqualTo(inventaire2);
        inventaire1.setId(null);
        assertThat(inventaire1).isNotEqualTo(inventaire2);
    }
}
