package com.um.umbook.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of FotoSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FotoSearchRepositoryMockConfiguration {

    @MockBean
    private FotoSearchRepository mockFotoSearchRepository;

}
