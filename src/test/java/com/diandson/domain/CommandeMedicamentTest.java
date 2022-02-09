package com.diandson.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommandeMedicamentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommandeMedicament.class);
        CommandeMedicament commandeMedicament1 = new CommandeMedicament();
        commandeMedicament1.setId(1L);
        CommandeMedicament commandeMedicament2 = new CommandeMedicament();
        commandeMedicament2.setId(commandeMedicament1.getId());
        assertThat(commandeMedicament1).isEqualTo(commandeMedicament2);
        commandeMedicament2.setId(2L);
        assertThat(commandeMedicament1).isNotEqualTo(commandeMedicament2);
        commandeMedicament1.setId(null);
        assertThat(commandeMedicament1).isNotEqualTo(commandeMedicament2);
    }
}
