package bookings.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import bookings.web.rest.TestUtil;

public class LuggageTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Luggage.class);
        Luggage luggage1 = new Luggage();
        luggage1.setId(1L);
        Luggage luggage2 = new Luggage();
        luggage2.setId(luggage1.getId());
        assertThat(luggage1).isEqualTo(luggage2);
        luggage2.setId(2L);
        assertThat(luggage1).isNotEqualTo(luggage2);
        luggage1.setId(null);
        assertThat(luggage1).isNotEqualTo(luggage2);
    }
}
