package com.github.javafaker;


import com.github.javafaker.service.RandomService;

import java.time.Clock;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class ISODateTime {
    public static final int MIN_OFFSET_TO_PAST_AND_FUTURE_MS = 1000;
    private final DateTimeGetter dateTimeGetter;
    private RandomService randomService;

    ISODateTime(RandomService randomService) {
        this(new DateTimeGetter(Clock.systemDefaultZone()), randomService);
    }

    ISODateTime(DateTimeGetter dateTimeGetter, RandomService randomService) {
        this.dateTimeGetter = dateTimeGetter;
        this.randomService = randomService;
    }

    /**
     * Returns a random future date-time
     *
     * <bold>Note</bold> that an offset of {@link #MIN_OFFSET_TO_PAST_AND_FUTURE_MS} is added to the
     * present date-time when generating this, so as to avoid returning values that are in the past
     * @param endOfTime The latest possible future date-time
     * @return A {@link ZonedDateTime} representing a random date and time from the future
     */
    public ZonedDateTime future(ZonedDateTime endOfTime) {
        final ZonedDateTime nearFuture = dateTimeGetter.now().plus(MIN_OFFSET_TO_PAST_AND_FUTURE_MS, ChronoUnit.MILLIS);
        return between(nearFuture, endOfTime);
    }


    /**
     * Returns a random past date-time
     *
     * <bold>Note</bold> that an offset of {@link #MIN_OFFSET_TO_PAST_AND_FUTURE_MS} is subtracted from the
     * present date-time when generating this, so as to avoid returning values that are in the present
     * @param beginningOfTime The earliest possible future date-time
     * @return A {@link ZonedDateTime} representing a random date and time from the past
     */
    public ZonedDateTime past(ZonedDateTime beginningOfTime) {
        final ZonedDateTime nearPast = dateTimeGetter.now().minus(MIN_OFFSET_TO_PAST_AND_FUTURE_MS, ChronoUnit.MILLIS);
        return between(beginningOfTime, nearPast);
    }

    /**
     * Returns a random date-time within a given range
     * @param from The earliest possible date-time
     * @param to The latest possible date-time
     * @return A {@link ZonedDateTime} representing a random date and time between {@code from} and {@code to}
     * @throws IllegalArgumentException if the range bounds are invalid
     */
    public ZonedDateTime between(ZonedDateTime from, ZonedDateTime to) throws IllegalArgumentException {
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Invalid date range, the upper bound date is before the lower bound.");
        }

        final long offsetMillis = randomService.nextLong(Duration.between(from, to).toMillis());
        return from.plus(offsetMillis, ChronoUnit.MILLIS);
    }

    static class DateTimeGetter {
        private final Clock clock;

        DateTimeGetter(Clock clock) {
            this.clock = clock;
        }

        ZonedDateTime now() {
            return ZonedDateTime.now(clock);
        }
    }
}
