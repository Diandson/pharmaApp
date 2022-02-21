package com.diandson.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VersementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VersementDTO.class);
        VersementDTO versementDTO1 = new VersementDTO();
        versementDTO1.setId(1L);
        VersementDTO versementDTO2 = new VersementDTO();
        assertThat(versementDTO1).isNotEqualTo(versementDTO2);
        versementDTO2.setId(versementDTO1.getId());
        assertThat(versementDTO1).isEqualTo(versementDTO2);
        versementDTO2.setId(2L);
        assertThat(versementDTO1).isNotEqualTo(versementDTO2);
        versementDTO1.setId(null);
        assertThat(versementDTO1).isNotEqualTo(versementDTO2);
    }
}
