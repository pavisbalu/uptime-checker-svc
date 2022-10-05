package in.pavisbalu.checkersvc.job.stages;

import in.pavisbalu.checkersvc.job.JobContext;
import in.pavisbalu.checkersvc.model.Check;
import in.pavisbalu.checkersvc.model.CheckRun;
import in.pavisbalu.checkersvc.model.ResultType;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;

/**
 * Second stage of processing that updates the last known status of the check
 */
@RequiredArgsConstructor
public class CandidatesForNotificationStage implements Runnable {

    private static final int LOOK_BACK_ITEMS = 3;
    private final CheckRun fetchResult;
    private final JobContext context;

    @Override
    public void run() {
        Check check = fetchResult.getCheck();
        List<CheckRun> runs = context.getCheckRunRepository().findTop3ByCheckOrderByCreatedAtDesc(check);
        if (runs.size() == LOOK_BACK_ITEMS) {
            runs.sort(Comparator.comparingLong(CheckRun::getCreatedAt));

            ResultType latestResult = runs.get(0).getResult();
            long responseTime = 0;
            boolean hasChanged = false;
            for (CheckRun run : runs) {
                responseTime += run.getResponseTimeInMillis();
                if (latestResult != run.getResult()) {
                    latestResult = run.getResult();
                    hasChanged = true;
                }
            }
            long avgResponseTime = responseTime / 3;

            // Add to Notification Queue iff, the last 3 results were same, and it is different
            // from the one before that. So this would send a notification when an app goes down
            // and also when it comes back up.
            if (!hasChanged && check.getStatus() != latestResult) {
                context.getCheckService().updateStatus(check, latestResult, avgResponseTime);
                context.nextStage(new NotificationStage(check, context, latestResult));
            } else {
                context.doneProcessingCheck();
            }
        } else {
            context.getCheckService().updateStatus(check, ResultType.IN_PROGRESS, 0L);
            context.doneProcessingCheck();
        }
    }
}
