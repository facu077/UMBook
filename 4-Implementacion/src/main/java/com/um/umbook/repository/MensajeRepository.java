package com.um.umbook.repository;

import com.um.umbook.domain.Mensaje;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Mensaje entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    @Query("select mensaje from Mensaje mensaje where mensaje.usuario.login = ?#{principal.username}")
    List<Mensaje> findByUsuarioIsCurrentUser();

}
