package in.pavisbalu.checkersvc.service;

import in.pavisbalu.checkersvc.model.Check;
import in.pavisbalu.checkersvc.model.CheckRun;
import in.pavisbalu.checkersvc.model.ResultType;
import in.pavisbalu.checkersvc.repository.CheckRepository;
import in.pavisbalu.checkersvc.repository.CheckRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CheckServiceImpl implements CheckService {
    @Autowired
    private CheckRepository checkRepository;
    @Autowired
    private CheckRunRepository checkRunRepository;

    @Override
    public Check save(Check check) {
        return checkRepository.save(check);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        checkRunRepository.deleteByCheckId(id);
        checkRepository.deleteById(id);
    }

    @Override
    public List<Check> findChecksByName(String nameSubstring) {
        return checkRepository.findByNameContainingIgnoreCase(nameSubstring);
    }

    @Override
    public Iterable<Check> all() {
        return checkRepository.findAll();
    }

    @Override
    public List<Check> findChecksByInterval(Duration duration) {
        return checkRepository.findByIntervalDuration(duration);
    }

    @Override
    public Optional<Check> findCheck(Long id) {
        return checkRepository.findById(id);
    }

    @Override
    public void activateCheck(Long checkId) {
        findCheck(checkId)
                .ifPresent(check -> {
                    check.activate();
                    save(check);
                });
    }

    @Override
    public void deactivateCheck(Long checkId) {
        findCheck(checkId)
                .ifPresent(check -> {
                    check.deactivate();
                    save(check);
                });
    }

    @Override
    public Check updateLastRan(Check check) {
        long lastRan = Instant.now().getEpochSecond();
        check.setLastRan(lastRan);
        return save(check);
    }

    @Override
    public void updateStatus(Check check, ResultType status, long avgResponseTime) {
        check.setStatus(status);
        check.setAvgResponseTime(avgResponseTime);
        check.setStatusSince(check.getLastRan());
        save(check);
    }

    @Override
    public List<Check> findChecksToExecute() {
        return checkRepository.findChecksToExecute();
    }

    @Override
    public List<CheckRun> history(Long checkId) {
        return findCheck(checkId)
                .map(check -> checkRunRepository.findByCheckOrderByCreatedAtDesc(check))
                .orElseGet(ArrayList::new);
    }

}
