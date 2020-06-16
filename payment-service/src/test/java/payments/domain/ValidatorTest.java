package payments.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import payments.web.rest.TestUtil;

public class ValidatorTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Validator.class);
        Validator validator1 = new Validator();
        validator1.setId(1L);
        Validator validator2 = new Validator();
        validator2.setId(validator1.getId());
        assertThat(validator1).isEqualTo(validator2);
        validator2.setId(2L);
        assertThat(validator1).isNotEqualTo(validator2);
        validator1.setId(null);
        assertThat(validator1).isNotEqualTo(validator2);
    }
}
