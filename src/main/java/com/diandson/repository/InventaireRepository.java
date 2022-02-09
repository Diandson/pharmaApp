package com.diandson.repository;

import com.diandson.domain.Inventaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Inventaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventaireRepository extends JpaRepository<Inventaire, Long> {}
