package com.github.javafaker;

import com.github.javafaker.service.FakeValuesServiceInterface;

public class Chemistry {
    private final FakeValuesServiceInterface fakeValuesService;

    public Chemistry(FakeValuesServiceInterface fakeValuesService) {
        this.fakeValuesService = fakeValuesService;
    }

    public String element() {
        return fakeValuesService.fetchString("chemistry.elements");
    }
}
