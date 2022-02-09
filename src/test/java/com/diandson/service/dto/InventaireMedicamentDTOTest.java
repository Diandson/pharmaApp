package com.diandson.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventaireMedicamentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InventaireMedicamentDTO.class);
        InventaireMedicamentDTO inventaireMedicamentDTO1 = new InventaireMedicamentDTO();
        inventaireMedicamentDTO1.setId(1L);
        InventaireMedicamentDTO inventaireMedicamentDTO2 = new InventaireMedicamentDTO();
        assertThat(inventaireMedicamentDTO1).isNotEqualTo(inventaireMedicamentDTO2);
        inventaireMedicamentDTO2.setId(inventaireMedicamentDTO1.getId());
        assertThat(inventaireMedicamentDTO1).isEqualTo(inventaireMedicamentDTO2);
        inventaireMedicamentDTO2.setId(2L);
        assertThat(inventaireMedicamentDTO1).isNotEqualTo(inventaireMedicamentDTO2);
        inventaireMedicamentDTO1.setId(null);
        assertThat(inventaireMedicamentDTO1).isNotEqualTo(inventaireMedicamentDTO2);
    }
}
