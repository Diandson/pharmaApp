package com.diandson.repository;

import com.diandson.domain.MotifListeDepense;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MotifListeDepense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotifListeDepenseRepository extends JpaRepository<MotifListeDepense, Long> {}
