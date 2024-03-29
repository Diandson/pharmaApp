package com.diandson.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepenseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Depense.class);
        Depense depense1 = new Depense();
        depense1.setId(1L);
        Depense depense2 = new Depense();
        depense2.setId(depense1.getId());
        assertThat(depense1).isEqualTo(depense2);
        depense2.setId(2L);
        assertThat(depense1).isNotEqualTo(depense2);
        depense1.setId(null);
        assertThat(depense1).isNotEqualTo(depense2);
    }
}
