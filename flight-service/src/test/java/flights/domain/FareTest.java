package flights.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import flights.web.rest.TestUtil;

public class FareTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fare.class);
        Fare fare1 = new Fare();
        fare1.setId(1L);
        Fare fare2 = new Fare();
        fare2.setId(fare1.getId());
        assertThat(fare1).isEqualTo(fare2);
        fare2.setId(2L);
        assertThat(fare1).isNotEqualTo(fare2);
        fare1.setId(null);
        assertThat(fare1).isNotEqualTo(fare2);
    }
}
