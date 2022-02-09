package com.diandson.repository;

import com.diandson.domain.Assurance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Assurance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssuranceRepository extends JpaRepository<Assurance, Long> {}
