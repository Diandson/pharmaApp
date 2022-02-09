package com.diandson.repository;

import com.diandson.domain.Approvisionnement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Approvisionnement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApprovisionnementRepository extends JpaRepository<Approvisionnement, Long> {}
