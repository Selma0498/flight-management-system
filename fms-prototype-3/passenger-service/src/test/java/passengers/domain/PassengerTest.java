package passengers.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import passengers.web.rest.TestUtil;

public class PassengerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Passenger.class);
        Passenger passenger1 = new Passenger("rand", "rand", "rand", "rand");
        passenger1.setId(1L);
        Passenger passenger2 = new Passenger("rand", "rand", "rand", "rand");
        passenger2.setId(passenger1.getId());
        assertThat(passenger1).isEqualTo(passenger2);
        passenger2.setId(2L);
        assertThat(passenger1).isNotEqualTo(passenger2);
        passenger1.setId(null);
        assertThat(passenger1).isNotEqualTo(passenger2);
    }
}
