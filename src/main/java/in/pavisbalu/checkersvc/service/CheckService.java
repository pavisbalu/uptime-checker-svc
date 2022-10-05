package in.pavisbalu.checkersvc.service;

import in.pavisbalu.checkersvc.model.Check;
import in.pavisbalu.checkersvc.model.CheckRun;
import in.pavisbalu.checkersvc.model.ResultType;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface CheckService {

    Check save(Check check);

    void delete(Long id);

    List<Check> findChecksByName(String nameSubstring);

    Iterable<Check> all();

    List<Check> findChecksByInterval(Duration intervalDuration);

    Optional<Check> findCheck(Long id);

    void activateCheck(Long checkId);

    void deactivateCheck(Long checkId);

    /**
     * Update the lastRan field in Check to current time in millis
     */
    Check updateLastRan(Check check);

    void updateStatus(Check check, ResultType status, long avgResponseTime);

    /**
     * Returns all the checks that need to be running. We identify something to be run if lastRan
     * has elapsed the interval unit.
     *
     * @return
     */
    List<Check> findChecksToExecute();

    /**
     * Return all the CheckRuns associated for the check identified by checkId
     *
     * @param checkId
     * @return
     */
    List<CheckRun> history(Long checkId);
}
