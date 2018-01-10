package com.foodfinder.utils;

import org.springframework.data.domain.PageRequest;

public class RestControllerTestUtils {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    public static PageRequest getDefaultPageRequest() {
        return new PageRequest(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
    }
}
