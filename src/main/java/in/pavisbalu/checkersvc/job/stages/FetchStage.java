package in.pavisbalu.checkersvc.job.stages;


import in.pavisbalu.checkersvc.job.JobContext;
import in.pavisbalu.checkersvc.model.Check;
import in.pavisbalu.checkersvc.model.CheckRun;
import in.pavisbalu.checkersvc.model.ResultType;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.Instant;

@RequiredArgsConstructor
public class FetchStage implements Runnable {
    private final Check check;
    private final JobContext context;

    @Override
    public void run() {
        Check updatedCheck = context.getCheckService().updateLastRan(check);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(updatedCheck.getUrl())
                .build();

        long start = System.nanoTime();
        CheckRun checkRun = new CheckRun();
        checkRun.setCheck(updatedCheck);
        checkRun.setCreatedAt(Instant.now().getEpochSecond());

        try (Response response = client.newCall(request).execute()) {
            checkRun.setResponseTimeInMillis(response.receivedResponseAtMillis());
            checkRun.setResult(ResultType.UP);
        } catch (IOException e) {
            checkRun.setResponseTimeInMillis((System.nanoTime() - start) / 1000);
            checkRun.setResult(ResultType.DOWN);
            checkRun.setErrorContext(e.getMessage());
        }
        CheckRun updatedCheckRun = context.getCheckRunRepository().save(checkRun);
        context.nextStage(new CandidatesForNotificationStage(updatedCheckRun, context));
    }
}
