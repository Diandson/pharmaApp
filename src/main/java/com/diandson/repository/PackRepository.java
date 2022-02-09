package com.diandson.repository;

import com.diandson.domain.Pack;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pack entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {}
