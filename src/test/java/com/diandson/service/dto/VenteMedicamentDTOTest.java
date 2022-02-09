package com.diandson.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VenteMedicamentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VenteMedicamentDTO.class);
        VenteMedicamentDTO venteMedicamentDTO1 = new VenteMedicamentDTO();
        venteMedicamentDTO1.setId(1L);
        VenteMedicamentDTO venteMedicamentDTO2 = new VenteMedicamentDTO();
        assertThat(venteMedicamentDTO1).isNotEqualTo(venteMedicamentDTO2);
        venteMedicamentDTO2.setId(venteMedicamentDTO1.getId());
        assertThat(venteMedicamentDTO1).isEqualTo(venteMedicamentDTO2);
        venteMedicamentDTO2.setId(2L);
        assertThat(venteMedicamentDTO1).isNotEqualTo(venteMedicamentDTO2);
        venteMedicamentDTO1.setId(null);
        assertThat(venteMedicamentDTO1).isNotEqualTo(venteMedicamentDTO2);
    }
}
