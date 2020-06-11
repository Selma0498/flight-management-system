package passenger.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link PassengerSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PassengerSearchRepositoryMockConfiguration {

    @MockBean
    private PassengerSearchRepository mockPassengerSearchRepository;

}
