package in.pavisbalu.checkersvc.repository;

import in.pavisbalu.checkersvc.model.Check;
import in.pavisbalu.checkersvc.model.CheckRun;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface CheckRunRepository extends JpaRepository<CheckRun, Long> {
    List<CheckRun> findTop3ByCheckOrderByCreatedAtDesc(Check check);

    List<CheckRun> findByCheckOrderByCreatedAtDesc(Check check);

    @Transactional
    void deleteByCheckId(Long checkId);
}
