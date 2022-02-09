package com.diandson.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diandson.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypePackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypePack.class);
        TypePack typePack1 = new TypePack();
        typePack1.setId(1L);
        TypePack typePack2 = new TypePack();
        typePack2.setId(typePack1.getId());
        assertThat(typePack1).isEqualTo(typePack2);
        typePack2.setId(2L);
        assertThat(typePack1).isNotEqualTo(typePack2);
        typePack1.setId(null);
        assertThat(typePack1).isNotEqualTo(typePack2);
    }
}
