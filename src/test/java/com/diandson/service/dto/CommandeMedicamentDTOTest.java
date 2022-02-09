package com.diandson.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommandeMedicamentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommandeMedicamentDTO.class);
        CommandeMedicamentDTO commandeMedicamentDTO1 = new CommandeMedicamentDTO();
        commandeMedicamentDTO1.setId(1L);
        CommandeMedicamentDTO commandeMedicamentDTO2 = new CommandeMedicamentDTO();
        assertThat(commandeMedicamentDTO1).isNotEqualTo(commandeMedicamentDTO2);
        commandeMedicamentDTO2.setId(commandeMedicamentDTO1.getId());
        assertThat(commandeMedicamentDTO1).isEqualTo(commandeMedicamentDTO2);
        commandeMedicamentDTO2.setId(2L);
        assertThat(commandeMedicamentDTO1).isNotEqualTo(commandeMedicamentDTO2);
        commandeMedicamentDTO1.setId(null);
        assertThat(commandeMedicamentDTO1).isNotEqualTo(commandeMedicamentDTO2);
    }
}
