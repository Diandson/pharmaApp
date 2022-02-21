package com.diandson.repository;

import com.diandson.domain.Versement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Versement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VersementRepository extends JpaRepository<Versement, Long> {}
