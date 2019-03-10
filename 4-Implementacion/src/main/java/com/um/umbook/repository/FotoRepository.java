package com.um.umbook.repository;

import com.um.umbook.domain.Foto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Foto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {

}
