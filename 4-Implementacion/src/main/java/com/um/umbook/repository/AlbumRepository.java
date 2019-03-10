package com.um.umbook.repository;

import com.um.umbook.domain.Album;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Album entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query("select album from Album album where album.usuario.login = ?#{principal.username}")
    List<Album> findByUsuarioIsCurrentUser();

}
