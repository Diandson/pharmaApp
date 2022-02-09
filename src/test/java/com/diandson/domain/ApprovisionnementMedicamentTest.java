package com.diandson.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApprovisionnementMedicamentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApprovisionnementMedicament.class);
        ApprovisionnementMedicament approvisionnementMedicament1 = new ApprovisionnementMedicament();
        approvisionnementMedicament1.setId(1L);
        ApprovisionnementMedicament approvisionnementMedicament2 = new ApprovisionnementMedicament();
        approvisionnementMedicament2.setId(approvisionnementMedicament1.getId());
        assertThat(approvisionnementMedicament1).isEqualTo(approvisionnementMedicament2);
        approvisionnementMedicament2.setId(2L);
        assertThat(approvisionnementMedicament1).isNotEqualTo(approvisionnementMedicament2);
        approvisionnementMedicament1.setId(null);
        assertThat(approvisionnementMedicament1).isNotEqualTo(approvisionnementMedicament2);
    }
}
