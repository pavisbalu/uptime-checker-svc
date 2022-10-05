package in.pavisbalu.checkersvc.job;

import in.pavisbalu.checkersvc.repository.CheckRunRepository;
import in.pavisbalu.checkersvc.service.CheckService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

@Getter
@RequiredArgsConstructor
public class JobContext {
    private final ExecutorService executorService;
    private final CountDownLatch counter;
    private final CheckService checkService;
    private final CheckRunRepository checkRunRepository;


    /**
     * Used to mark completion of all the stages for a given Check input
     */
    public void doneProcessingCheck() {
        counter.countDown();
    }

    public void nextStage(Runnable nextStage) {
        executorService.submit(nextStage);
    }
}
