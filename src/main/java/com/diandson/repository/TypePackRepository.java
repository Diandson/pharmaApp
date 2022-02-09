package com.diandson.repository;

import com.diandson.domain.TypePack;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypePack entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypePackRepository extends JpaRepository<TypePack, Long> {}
