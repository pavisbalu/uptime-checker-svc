# Website Monitoring Tool

## Design

Current implementation has 2 entities: `Check` and `CheckRun`. Check represents each check a user would like to define
for a URL that they need to monitor. CheckRun represents an instance of such a check that was run at a given time.

Apart from the usual Controller, Service and Repository abstractions from Spring, we have a Scheduler that runs at a
fixed rate to execute a pipeline (ie. a set of stages) for each check which runs the actual check and also the
notification (if required) as part of the process.

The pipeline has 3 stages:

- FetchStage - Runs the actual check by fetching the URL for each check
- CheckForNotificationStage - Checks from the run history if we need to send notification for the given check
- NotificationStage - This does the actual notification today.

We've a `Notifier` interface that can support different type of notifier implementations for each check. Think of each
user wanting a different mode of alerting for each check. Today we've implemented only LogNotifier which writes to our
standard logger.

## API Endpoints

| Endpoints                    | Method | Description                                                                                                                                                                                                                                                                             |
|------------------------------|--------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/api/check`                 | POST   | Create a new Check with JSON Payload. Check the next section on sample payloads for this API.                                                                                                                                                                                           |
| `/api/check`                 | GET    | Displays a list of checks based on `name` or `interval` query params. <br/>- `name` does a case ignored match against the name of the checks. <br/>- `interval` is a [Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#parse-java.lang.CharSequence-) format |
| `/api/check/{id}`            | DELETE | Delete an existing Check                                                                                                                                                                                                                                                                |
| `/api/check/{id}/history`    | GET    | Returns the list of history runs associated with the Check identified by `id`                                                                                                                                                                                                           |
| `/api/check/{id}/activate`   | POST   | Activate a check (if deactivated). It is a no-op if the check is already active                                                                                                                                                                                                         |
| `/api/check/{id}/deactivate` | POST   | De-Activate a check (if activated). It is a no-op if the check is already de-active                                                                                                                                                                                                     |

## Sample Check Payloads

### Check that always fails

```json
{
  "name": "Check That Fails",
  "isActive": true,
  "intervalDuration": "PT10S",
  "connectionTimeout": 1000,
  "url": "http://localhost:12345/"
}
```

### Check that always pass

```json
{
  "name": "Check That Pass",
  "isActive": true,
  "intervalDuration": "PT1H",
  "connectionTimeout": 1000,
  "url": "http://localhost:8080/"
}
```

### Generic Website

```json
{
  "name": "Wikipedia Check",
  "isActive": true,
  "intervalDuration": "PT5M",
  "connectionTimeout": 1000,
  "url": "http://en.wikipedia.org/"
}
```

## Additional Notes

There were quite a few items in the problem statement that I didn't get a chance to implement in the 24 hour period.
They are as follows:

- JUnit Test cases.
- Checks associated with Users. Today all Checks are global and there is no user context implemented, but if needed can
  add those to by creating a OneToMany relationship with the Check entity.
- Since there aren't any users, we also don't have any security.
- Docker image & other deployment patterns
- No UI was built for this App.
