package com.diandson.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VersementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Versement.class);
        Versement versement1 = new Versement();
        versement1.setId(1L);
        Versement versement2 = new Versement();
        versement2.setId(versement1.getId());
        assertThat(versement1).isEqualTo(versement2);
        versement2.setId(2L);
        assertThat(versement1).isNotEqualTo(versement2);
        versement1.setId(null);
        assertThat(versement1).isNotEqualTo(versement2);
    }
}
