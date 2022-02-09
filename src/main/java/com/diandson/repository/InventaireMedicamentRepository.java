package com.diandson.repository;

import com.diandson.domain.InventaireMedicament;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InventaireMedicament entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventaireMedicamentRepository extends JpaRepository<InventaireMedicament, Long> {}
