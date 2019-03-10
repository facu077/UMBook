package com.um.umbook.repository.search;

import com.um.umbook.domain.Foto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Foto entity.
 */
public interface FotoSearchRepository extends ElasticsearchRepository<Foto, Long> {
}
