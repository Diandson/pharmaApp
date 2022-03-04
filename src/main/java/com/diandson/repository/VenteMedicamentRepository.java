package com.diandson.repository;

import com.diandson.domain.VenteMedicament;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the VenteMedicament entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VenteMedicamentRepository extends JpaRepository<VenteMedicament, Long> {
    List<VenteMedicament> findAllByVenteId(Long id);
}
