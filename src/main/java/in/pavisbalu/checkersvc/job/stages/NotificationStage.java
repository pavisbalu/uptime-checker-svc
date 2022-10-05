package in.pavisbalu.checkersvc.job.stages;

import in.pavisbalu.checkersvc.job.JobContext;
import in.pavisbalu.checkersvc.model.Check;
import in.pavisbalu.checkersvc.model.ResultType;
import in.pavisbalu.checkersvc.notifier.Notifier;
import in.pavisbalu.checkersvc.notifier.NotifierFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationStage implements Runnable {
    private final Check check;
    private final JobContext context;
    private final ResultType latestResult;

    @Override
    public void run() {
        Notifier notifier = NotifierFactory.notifierFrom(check.getNotificationType());
        notifier.notify(check, latestResult);

        context.doneProcessingCheck();
    }
}
