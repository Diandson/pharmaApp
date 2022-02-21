package com.diandson.repository;

import com.diandson.domain.LieuVersement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LieuVersement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LieuVersementRepository extends JpaRepository<LieuVersement, Long> {}
