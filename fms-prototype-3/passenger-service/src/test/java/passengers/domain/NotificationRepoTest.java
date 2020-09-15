package passengers.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import passengers.web.rest.TestUtil;

public class NotificationRepoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationRepo.class);
        NotificationRepo notificationRepo1 = new NotificationRepo("rand", "rand");
        notificationRepo1.setId(1L);
        NotificationRepo notificationRepo2 = new NotificationRepo("rand", "rand");
        notificationRepo2.setId(notificationRepo1.getId());
        assertThat(notificationRepo1).isEqualTo(notificationRepo2);
        notificationRepo2.setId(2L);
        assertThat(notificationRepo1).isNotEqualTo(notificationRepo2);
        notificationRepo1.setId(null);
        assertThat(notificationRepo1).isNotEqualTo(notificationRepo2);
    }
}
