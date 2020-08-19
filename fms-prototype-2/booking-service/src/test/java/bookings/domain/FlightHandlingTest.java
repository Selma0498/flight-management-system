package bookings.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import bookings.web.rest.TestUtil;

public class FlightHandlingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlightHandling.class);
        FlightHandling flightHandling1 = new FlightHandling();
        flightHandling1.setId(1L);
        FlightHandling flightHandling2 = new FlightHandling();
        flightHandling2.setId(flightHandling1.getId());
        assertThat(flightHandling1).isEqualTo(flightHandling2);
        flightHandling2.setId(2L);
        assertThat(flightHandling1).isNotEqualTo(flightHandling2);
        flightHandling1.setId(null);
        assertThat(flightHandling1).isNotEqualTo(flightHandling2);
    }
}
