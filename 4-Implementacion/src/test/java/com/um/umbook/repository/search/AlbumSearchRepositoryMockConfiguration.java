package com.um.umbook.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of AlbumSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AlbumSearchRepositoryMockConfiguration {

    @MockBean
    private AlbumSearchRepository mockAlbumSearchRepository;

}
