package com.diandson.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApprovisionnementMedicamentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovisionnementMedicamentDTO.class);
        ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO1 = new ApprovisionnementMedicamentDTO();
        approvisionnementMedicamentDTO1.setId(1L);
        ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO2 = new ApprovisionnementMedicamentDTO();
        assertThat(approvisionnementMedicamentDTO1).isNotEqualTo(approvisionnementMedicamentDTO2);
        approvisionnementMedicamentDTO2.setId(approvisionnementMedicamentDTO1.getId());
        assertThat(approvisionnementMedicamentDTO1).isEqualTo(approvisionnementMedicamentDTO2);
        approvisionnementMedicamentDTO2.setId(2L);
        assertThat(approvisionnementMedicamentDTO1).isNotEqualTo(approvisionnementMedicamentDTO2);
        approvisionnementMedicamentDTO1.setId(null);
        assertThat(approvisionnementMedicamentDTO1).isNotEqualTo(approvisionnementMedicamentDTO2);
    }
}
