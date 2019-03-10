package com.um.umbook.repository;

import com.um.umbook.domain.Amigo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Amigo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AmigoRepository extends JpaRepository<Amigo, Long> {

    @Query("select amigo from Amigo amigo where amigo.usuario.login = ?#{principal.username}")
    List<Amigo> findByUsuarioIsCurrentUser();

    @Query("select amigo from Amigo amigo where amigo.amigo.login = ?#{principal.username}")
    List<Amigo> findByAmigoIsCurrentUser();

}
