package com.um.umbook.repository.search;

import com.um.umbook.domain.Album;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Album entity.
 */
public interface AlbumSearchRepository extends ElasticsearchRepository<Album, Long> {
}
