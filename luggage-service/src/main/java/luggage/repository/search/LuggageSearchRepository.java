package luggage.repository.search;

import luggage.domain.Luggage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Luggage} entity.
 */
public interface LuggageSearchRepository extends ElasticsearchRepository<Luggage, Long> {
}
