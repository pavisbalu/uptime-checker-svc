package in.pavisbalu.checkersvc.notifier;

import in.pavisbalu.checkersvc.model.Check;
import in.pavisbalu.checkersvc.model.ResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogNotifier implements Notifier {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void notify(Check check, ResultType lastResult) {
        LOG.error("{} against url: {} is {}!", check.getName(), check.getUrl(), lastResult);
    }
}
