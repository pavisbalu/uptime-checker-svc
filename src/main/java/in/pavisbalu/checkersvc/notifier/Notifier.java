package in.pavisbalu.checkersvc.notifier;

import in.pavisbalu.checkersvc.model.Check;
import in.pavisbalu.checkersvc.model.ResultType;

public interface Notifier {
    /**
     * Send a notification for a given check. Notification configurations are part of the Check itself
     * for the implementations that require it.
     *
     * @param check
     * @param lastResult
     */
    void notify(Check check, ResultType lastResult);
}
