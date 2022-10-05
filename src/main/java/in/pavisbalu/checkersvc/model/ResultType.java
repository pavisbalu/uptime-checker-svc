package in.pavisbalu.checkersvc.model;

public enum ResultType {
    /**
     * URL is alive and well.
     */
    UP,
    /**
     * URL was down or we were unable to reach it from our network
     */
    DOWN,
    /**
     * Initial result type for a check until we've at least 3 results of check runs
     */
    IN_PROGRESS
}
