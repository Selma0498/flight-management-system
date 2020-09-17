package passengers.repository;

import passengers.domain.NotificationRepo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the NotificationRepo entity.
 */
@Repository
public interface NotificationRepoRepository extends JpaRepository<NotificationRepo, Long> {

    @Query(value = "select distinct notificationRepo from NotificationRepo notificationRepo left join fetch notificationRepo.passengers",
        countQuery = "select count(distinct notificationRepo) from NotificationRepo notificationRepo")
    Page<NotificationRepo> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct notificationRepo from NotificationRepo notificationRepo left join fetch notificationRepo.passengers")
    List<NotificationRepo> findAllWithEagerRelationships();

    @Query("select notificationRepo from NotificationRepo notificationRepo left join fetch notificationRepo.passengers where notificationRepo.id =:id")
    Optional<NotificationRepo> findOneWithEagerRelationships(@Param("id") Long id);
}
