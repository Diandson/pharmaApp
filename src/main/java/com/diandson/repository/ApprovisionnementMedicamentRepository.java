package com.diandson.repository;

import com.diandson.domain.ApprovisionnementMedicament;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApprovisionnementMedicament entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApprovisionnementMedicamentRepository extends JpaRepository<ApprovisionnementMedicament, Long> {}
