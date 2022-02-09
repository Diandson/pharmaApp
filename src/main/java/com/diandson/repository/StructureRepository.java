package com.diandson.repository;

import com.diandson.domain.Structure;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Structure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StructureRepository extends JpaRepository<Structure, Long> {}
