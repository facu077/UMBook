package com.um.umbook.repository.search;

import com.um.umbook.domain.Mensaje;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Mensaje entity.
 */
public interface MensajeSearchRepository extends ElasticsearchRepository<Mensaje, Long> {
}
