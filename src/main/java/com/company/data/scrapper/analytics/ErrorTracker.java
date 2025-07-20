package com.company.data.scrapper.analytics;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ErrorTracker {

    private final ConcurrentHashMap<String, AtomicInteger> errorCounts = new ConcurrentHashMap<>();

    public void logError(String errorMessage) {
        errorCounts.computeIfAbsent(errorMessage, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void printErrorCounts() {
        errorCounts.forEach((error, count) ->
                System.out.println(error + " -> " + count.get() + " times"));
    }
}
