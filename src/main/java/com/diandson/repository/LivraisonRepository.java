package com.diandson.repository;

import com.diandson.domain.Livraison;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Livraison entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long> {}
