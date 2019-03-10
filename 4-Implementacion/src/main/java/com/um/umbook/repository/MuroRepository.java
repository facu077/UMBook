package com.um.umbook.repository;

import com.um.umbook.domain.Muro;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Muro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MuroRepository extends JpaRepository<Muro, Long> {

}
