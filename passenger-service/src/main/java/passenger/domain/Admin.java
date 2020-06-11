package passenger.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A Admin.
 */
@Entity
@Table(name = "admin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "admin")
public class Admin extends User {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    public Admin(ERole role, Credentials credentials) {
        super(role, credentials);
    }

    public Admin(Credentials credentials) {
        this.setRole(ERole.ADMIN);
        this.setCredentials(credentials);

        this.id = super.getId();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    // TODO: add updateFlightInfo and cancelBooking functionalities
}
