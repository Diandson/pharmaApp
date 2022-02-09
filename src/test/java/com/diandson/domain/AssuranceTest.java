package com.diandson.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssuranceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assurance.class);
        Assurance assurance1 = new Assurance();
        assurance1.setId(1L);
        Assurance assurance2 = new Assurance();
        assurance2.setId(assurance1.getId());
        assertThat(assurance1).isEqualTo(assurance2);
        assurance2.setId(2L);
        assertThat(assurance1).isNotEqualTo(assurance2);
        assurance1.setId(null);
        assertThat(assurance1).isNotEqualTo(assurance2);
    }
}
