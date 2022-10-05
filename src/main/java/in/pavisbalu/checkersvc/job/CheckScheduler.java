package in.pavisbalu.checkersvc.job;

import in.pavisbalu.checkersvc.job.stages.FetchStage;
import in.pavisbalu.checkersvc.model.Check;
import in.pavisbalu.checkersvc.repository.CheckRunRepository;
import in.pavisbalu.checkersvc.service.CheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class CheckScheduler {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final Logger LOG = LoggerFactory.getLogger(CheckScheduler.class);
    @Autowired
    private CheckService checkService;
    @Autowired
    private CheckRunRepository checkRunRepository;

    /**
     * We run a scheduler every 10 seconds, to determine all the checks that need to be run and start processing
     * them. We run all checks in parallel (bounded to size of fixed thread pool).
     */
    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    public void schedule() {
        List<Check> checks = checkService.findChecksToExecute();
        CountDownLatch counter = new CountDownLatch(checks.size());
        JobContext context = new JobContext(executorService, counter, checkService, checkRunRepository);

        /*
          We run a pipeline (of sorts) as part of this scheduler. Roughly the stages are as follows:
          1. Fetch (the URL)
          2. Check the candidates for notification
          3. Notify (checks that match the criteria)

          Without introducing additional complexity we implement Pipeline abstraction using a loosely
          chained Runnable Tasks. Each Runnable has access to a JobContext that has an ExecutorService
          to which we append the next stage. We have a CountDownLatch to keep track of each Check
          that is being processed.
         */
        for (Check check : checks) {
            context.nextStage(new FetchStage(check, context));
        }

        try {
            counter.await();
        } catch (InterruptedException e) {
            // don't want to bring down the app because of poller job error,
            // it is always safe to retry the whole process
            LOG.error(e.getMessage(), e);
        }
    }
}
