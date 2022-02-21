package com.diandson.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LieuVersementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LieuVersement.class);
        LieuVersement lieuVersement1 = new LieuVersement();
        lieuVersement1.setId(1L);
        LieuVersement lieuVersement2 = new LieuVersement();
        lieuVersement2.setId(lieuVersement1.getId());
        assertThat(lieuVersement1).isEqualTo(lieuVersement2);
        lieuVersement2.setId(2L);
        assertThat(lieuVersement1).isNotEqualTo(lieuVersement2);
        lieuVersement1.setId(null);
        assertThat(lieuVersement1).isNotEqualTo(lieuVersement2);
    }
}
