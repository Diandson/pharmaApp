package com.diandson.repository;

import com.diandson.domain.Medicament;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Medicament entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {}
