package com.diandson.repository;

import com.diandson.domain.VenteMedicament;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VenteMedicament entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VenteMedicamentRepository extends JpaRepository<VenteMedicament, Long> {}
