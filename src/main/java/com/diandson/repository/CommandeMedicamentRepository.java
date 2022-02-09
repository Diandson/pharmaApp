package com.diandson.repository;

import com.diandson.domain.CommandeMedicament;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CommandeMedicament entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommandeMedicamentRepository extends JpaRepository<CommandeMedicament, Long> {}
