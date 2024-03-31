package edu.java.backoff_policy;

import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.BackOffPolicy;

public class LinearBackOffPolicy implements BackOffPolicy {

    private final long initialInterval;
    private final int maxAttempts;
    private final long increment;

    public LinearBackOffPolicy(long initialInterval, int maxAttempts, long increment) {
        this.initialInterval = initialInterval;
        this.maxAttempts = maxAttempts;
        this.increment = increment;
    }

    @Override
    public BackOffContext start(RetryContext context) {
        return new LinearBackOffContext(initialInterval, increment, maxAttempts);
    }

    @Override
    public void backOff(BackOffContext backOffContext) throws BackOffInterruptedException {
        LinearBackOffContext context = (LinearBackOffContext) backOffContext;
        long sleepTime = context.getSleepAndIncrement();
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static class LinearBackOffContext implements BackOffContext {
        private long nextInterval;
        private final long increment;
        private final long maxInterval;

        LinearBackOffContext(long initialInterval, long increment, long maxInterval) {
            this.nextInterval = initialInterval;
            this.increment = increment;
            this.maxInterval = maxInterval;
        }

        public long getSleepAndIncrement() {
            long sleep = this.nextInterval;
            this.nextInterval = Math.min(this.nextInterval + this.increment, this.maxInterval);
            return sleep;
        }
    }
}
