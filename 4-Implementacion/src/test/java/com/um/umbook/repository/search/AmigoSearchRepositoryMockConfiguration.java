package com.um.umbook.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of AmigoSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AmigoSearchRepositoryMockConfiguration {

    @MockBean
    private AmigoSearchRepository mockAmigoSearchRepository;

}
