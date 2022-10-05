package in.pavisbalu.checkersvc.notifier;

import org.springframework.stereotype.Component;

@Component
public class NotifierFactory {
    public static Notifier notifierFrom(String type) {
        switch (type) {
            case "log":
                return new LogNotifier();
            // TODO: Add more notifier implementations like email, slack, teams, webhooks, etc.
            default:
                throw new RuntimeException("Unidentified notifier type: " + type);
        }
    }
}
