package com.diandson.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MotifListeDepenseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MotifListeDepense.class);
        MotifListeDepense motifListeDepense1 = new MotifListeDepense();
        motifListeDepense1.setId(1L);
        MotifListeDepense motifListeDepense2 = new MotifListeDepense();
        motifListeDepense2.setId(motifListeDepense1.getId());
        assertThat(motifListeDepense1).isEqualTo(motifListeDepense2);
        motifListeDepense2.setId(2L);
        assertThat(motifListeDepense1).isNotEqualTo(motifListeDepense2);
        motifListeDepense1.setId(null);
        assertThat(motifListeDepense1).isNotEqualTo(motifListeDepense2);
    }
}
