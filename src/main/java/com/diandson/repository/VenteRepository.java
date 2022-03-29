package com.diandson.repository;

import com.diandson.domain.Vente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data SQL repository for the Vente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VenteRepository extends JpaRepository<Vente, Long> {
    List<Vente> findAllByDateVenteBetween(ZonedDateTime from, ZonedDateTime to);
}
