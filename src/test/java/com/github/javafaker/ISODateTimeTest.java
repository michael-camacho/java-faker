package com.github.javafaker;

import com.github.javafaker.ISODateTime.DateTimeGetter;
import com.github.javafaker.service.RandomService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Created by michael on 26/09/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ISODateTimeTest {
    private final ZonedDateTime PRESENT = ZonedDateTime.parse("2016-09-26T10:30:45+01:00[Europe/Paris]");
    private final TemporalAmount DATE_RANGE = Period.ofYears(10); // Arbitrary test value for generating ranges

    private ISODateTime isoDateTime;
    @Mock
    private DateTimeGetter dateTimeGetter;
    @Mock
    private RandomService randomService;

    @Before
    public void before() {
        when(dateTimeGetter.now()).thenReturn(PRESENT);
        isoDateTime = new ISODateTime(dateTimeGetter, randomService);
    }

    @Test
    public void future_midpoint() throws Exception {
        when(randomService.nextLong(anyLong())).thenAnswer(inv -> (long) inv.getArguments()[0] / 2); // Return the middle value

        final ZonedDateTime endOfTime = PRESENT.plus(DATE_RANGE);
        ZonedDateTime future = isoDateTime.future(endOfTime);
        assertTrue(future.isAfter(PRESENT));
        assertFalse(future.isAfter(endOfTime));
    }

    @Test
    public void future_closest() throws Exception {
        when(randomService.nextLong(anyLong())).thenAnswer(inv -> 0L);                  // For oldest value

        final ZonedDateTime endOfTime = PRESENT.plus(DATE_RANGE);
        ZonedDateTime future = isoDateTime.future(endOfTime);
        assertTrue(future.isEqual(PRESENT.plus(ISODateTime.MIN_OFFSET_TO_PAST_AND_FUTURE_MS, ChronoUnit.MILLIS)));
    }

    @Test
    public void future_furthest() throws Exception {
        when(randomService.nextLong(anyLong())).thenAnswer(inv -> inv.getArguments()[0]); // Return the middle value

        final ZonedDateTime endOfTime = PRESENT.plus(DATE_RANGE);
        ZonedDateTime future = isoDateTime.future(endOfTime);
        assertTrue(future.isEqual(endOfTime));
    }

    @Test
    public void past_midpoint() throws Exception {
        when(randomService.nextLong(anyLong())).thenAnswer(inv -> (long) inv.getArguments()[0] / 2); // Return the middle value

        final ZonedDateTime beginningOfTime = PRESENT.minus(DATE_RANGE);
        ZonedDateTime past = isoDateTime.past(beginningOfTime);
        assertFalse(past.isBefore(beginningOfTime));
        assertTrue(past.isBefore(PRESENT));
    }

    @Test
    public void past_closest() throws Exception {
        when(randomService.nextLong(anyLong())).thenAnswer(inv -> inv.getArguments()[0]); // For newest value

        final ZonedDateTime beginningOfTime = PRESENT.minus(DATE_RANGE);
        ZonedDateTime past = isoDateTime.past(beginningOfTime);
        assertTrue(past.isEqual(PRESENT.minus(ISODateTime.MIN_OFFSET_TO_PAST_AND_FUTURE_MS, ChronoUnit.MILLIS)));
    }

    @Test
    public void past_furthest() throws Exception {
        when(randomService.nextLong(anyLong())).thenAnswer(inv -> 0L);                  // For oldest value

        final ZonedDateTime beginningOfTime = PRESENT.minus(DATE_RANGE);
        ZonedDateTime past = isoDateTime.past(beginningOfTime);
        assertTrue(past.isEqual(beginningOfTime));
    }

    @Test
    public void between_midpoint() throws Exception {
        when(randomService.nextLong(anyLong())).thenAnswer(inv -> (long) inv.getArguments()[0] / 2); // Return the middle value

        final ZonedDateTime from = PRESENT.minus(DATE_RANGE);
        final ZonedDateTime to  = PRESENT.plus(DATE_RANGE);
        ZonedDateTime result = isoDateTime.between(from, to);
        assertFalse(result.isBefore(from));
        assertFalse(result.isAfter(to));
    }

    public void between_earliest() throws Exception {
        when(randomService.nextLong(anyLong())).thenAnswer(inv -> 0L);                  // For oldest value

        final ZonedDateTime from = PRESENT.minus(DATE_RANGE);
        final ZonedDateTime to  = PRESENT.plus(DATE_RANGE);
        ZonedDateTime result = isoDateTime.between(from, to);
        assertTrue(result.isEqual(from));
    }

    public void between_latest() throws Exception {
        when(randomService.nextLong(anyLong())).thenAnswer(inv -> inv.getArguments()[0]); // For newest value

        final ZonedDateTime from = PRESENT.minus(DATE_RANGE);
        final ZonedDateTime to  = PRESENT.plus(DATE_RANGE);
        ZonedDateTime result = isoDateTime.between(from, to);
        assertTrue(result.isEqual(to));
    }
}