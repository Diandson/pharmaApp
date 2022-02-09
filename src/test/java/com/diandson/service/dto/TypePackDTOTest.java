package com.diandson.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypePackDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypePackDTO.class);
        TypePackDTO typePackDTO1 = new TypePackDTO();
        typePackDTO1.setId(1L);
        TypePackDTO typePackDTO2 = new TypePackDTO();
        assertThat(typePackDTO1).isNotEqualTo(typePackDTO2);
        typePackDTO2.setId(typePackDTO1.getId());
        assertThat(typePackDTO1).isEqualTo(typePackDTO2);
        typePackDTO2.setId(2L);
        assertThat(typePackDTO1).isNotEqualTo(typePackDTO2);
        typePackDTO1.setId(null);
        assertThat(typePackDTO1).isNotEqualTo(typePackDTO2);
    }
}
