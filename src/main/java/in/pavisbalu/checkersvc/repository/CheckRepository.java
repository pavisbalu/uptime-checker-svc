package in.pavisbalu.checkersvc.repository;

import in.pavisbalu.checkersvc.model.Check;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Duration;
import java.util.List;

public interface CheckRepository extends JpaRepository<Check, Long> {
    /**
     * Find checks containing the given name by doing case in-sensitive search.
     *
     * @param name
     * @return
     */
    List<Check> findByNameContainingIgnoreCase(String name);

    /**
     * Find checks containing the given interval specification.
     *
     * @param intervalDuration
     * @return
     */
    List<Check> findByIntervalDuration(Duration intervalDuration);

    /**
     * Find checks that need to be executed at any given point in time.
     *
     * @return
     */
    // we store all the interval in seconds (since it is easy to test locally)
    @Query(value = "SELECT c.* FROM checks c where c.is_active AND (c.last_ran is NULL OR EXTRACT(EPOCH FROM CURRENT_TIMESTAMP(0)) > (c.last_ran + c.interval_duration))", nativeQuery = true)
    List<Check> findChecksToExecute();
}
