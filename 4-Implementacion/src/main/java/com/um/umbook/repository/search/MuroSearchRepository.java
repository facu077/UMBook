package com.um.umbook.repository.search;

import com.um.umbook.domain.Muro;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Muro entity.
 */
public interface MuroSearchRepository extends ElasticsearchRepository<Muro, Long> {
}
