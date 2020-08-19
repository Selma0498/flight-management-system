package flights.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import flights.web.rest.TestUtil;

public class AirlineTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Airline.class);
        Airline airline1 = new Airline();
        airline1.setId(1L);
        Airline airline2 = new Airline();
        airline2.setId(airline1.getId());
        assertThat(airline1).isEqualTo(airline2);
        airline2.setId(2L);
        assertThat(airline1).isNotEqualTo(airline2);
        airline1.setId(null);
        assertThat(airline1).isNotEqualTo(airline2);
    }
}
