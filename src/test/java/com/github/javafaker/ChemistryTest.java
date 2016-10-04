package com.github.javafaker;

import org.junit.Before;
import org.junit.Test;

import static com.github.javafaker.matchers.MatchesRegularExpression.matchesRegularExpression;
import static org.junit.Assert.*;

public class ChemistryTest {
    private Faker faker;

    @Before
    public void before() {
        faker = new Faker();
    }

    @Test
    public void element() throws Exception {
        assertThat(faker.chemistry().element(), matchesRegularExpression("^[A-Z][a-z]{3,}$"));
    }

}