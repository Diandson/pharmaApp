package com.diandson.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VenteMedicamentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VenteMedicament.class);
        VenteMedicament venteMedicament1 = new VenteMedicament();
        venteMedicament1.setId(1L);
        VenteMedicament venteMedicament2 = new VenteMedicament();
        venteMedicament2.setId(venteMedicament1.getId());
        assertThat(venteMedicament1).isEqualTo(venteMedicament2);
        venteMedicament2.setId(2L);
        assertThat(venteMedicament1).isNotEqualTo(venteMedicament2);
        venteMedicament1.setId(null);
        assertThat(venteMedicament1).isNotEqualTo(venteMedicament2);
    }
}
