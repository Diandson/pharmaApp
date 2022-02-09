package com.diandson.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApprovisionnementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovisionnementDTO.class);
        ApprovisionnementDTO approvisionnementDTO1 = new ApprovisionnementDTO();
        approvisionnementDTO1.setId(1L);
        ApprovisionnementDTO approvisionnementDTO2 = new ApprovisionnementDTO();
        assertThat(approvisionnementDTO1).isNotEqualTo(approvisionnementDTO2);
        approvisionnementDTO2.setId(approvisionnementDTO1.getId());
        assertThat(approvisionnementDTO1).isEqualTo(approvisionnementDTO2);
        approvisionnementDTO2.setId(2L);
        assertThat(approvisionnementDTO1).isNotEqualTo(approvisionnementDTO2);
        approvisionnementDTO1.setId(null);
        assertThat(approvisionnementDTO1).isNotEqualTo(approvisionnementDTO2);
    }
}
