package com.diandson.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MotifListeDepenseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MotifListeDepenseDTO.class);
        MotifListeDepenseDTO motifListeDepenseDTO1 = new MotifListeDepenseDTO();
        motifListeDepenseDTO1.setId(1L);
        MotifListeDepenseDTO motifListeDepenseDTO2 = new MotifListeDepenseDTO();
        assertThat(motifListeDepenseDTO1).isNotEqualTo(motifListeDepenseDTO2);
        motifListeDepenseDTO2.setId(motifListeDepenseDTO1.getId());
        assertThat(motifListeDepenseDTO1).isEqualTo(motifListeDepenseDTO2);
        motifListeDepenseDTO2.setId(2L);
        assertThat(motifListeDepenseDTO1).isNotEqualTo(motifListeDepenseDTO2);
        motifListeDepenseDTO1.setId(null);
        assertThat(motifListeDepenseDTO1).isNotEqualTo(motifListeDepenseDTO2);
    }
}
