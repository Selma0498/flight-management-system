package passenger.repository.search;

import passenger.domain.Passenger;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Passenger} entity.
 */
public interface PassengerSearchRepository extends ElasticsearchRepository<Passenger, Long> {
}
