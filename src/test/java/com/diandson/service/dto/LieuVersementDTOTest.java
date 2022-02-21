package com.diandson.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LieuVersementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LieuVersementDTO.class);
        LieuVersementDTO lieuVersementDTO1 = new LieuVersementDTO();
        lieuVersementDTO1.setId(1L);
        LieuVersementDTO lieuVersementDTO2 = new LieuVersementDTO();
        assertThat(lieuVersementDTO1).isNotEqualTo(lieuVersementDTO2);
        lieuVersementDTO2.setId(lieuVersementDTO1.getId());
        assertThat(lieuVersementDTO1).isEqualTo(lieuVersementDTO2);
        lieuVersementDTO2.setId(2L);
        assertThat(lieuVersementDTO1).isNotEqualTo(lieuVersementDTO2);
        lieuVersementDTO1.setId(null);
        assertThat(lieuVersementDTO1).isNotEqualTo(lieuVersementDTO2);
    }
}
