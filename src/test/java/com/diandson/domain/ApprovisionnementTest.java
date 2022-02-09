package com.diandson.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApprovisionnementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Approvisionnement.class);
        Approvisionnement approvisionnement1 = new Approvisionnement();
        approvisionnement1.setId(1L);
        Approvisionnement approvisionnement2 = new Approvisionnement();
        approvisionnement2.setId(approvisionnement1.getId());
        assertThat(approvisionnement1).isEqualTo(approvisionnement2);
        approvisionnement2.setId(2L);
        assertThat(approvisionnement1).isNotEqualTo(approvisionnement2);
        approvisionnement1.setId(null);
        assertThat(approvisionnement1).isNotEqualTo(approvisionnement2);
    }
}
