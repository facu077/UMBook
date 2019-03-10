package com.um.umbook.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of MuroSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class MuroSearchRepositoryMockConfiguration {

    @MockBean
    private MuroSearchRepository mockMuroSearchRepository;

}
