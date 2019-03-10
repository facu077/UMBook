package com.um.umbook.repository.search;

import com.um.umbook.domain.Amigo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Amigo entity.
 */
public interface AmigoSearchRepository extends ElasticsearchRepository<Amigo, Long> {
}
