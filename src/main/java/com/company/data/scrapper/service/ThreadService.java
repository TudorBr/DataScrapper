package com.company.data.scrapper.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ThreadService {

    private final DataScrapperService dataScrapperService;
    private static final Logger logger = LoggerFactory.getLogger(ThreadService.class);

    public ThreadService(DataScrapperService dataScrapperService) {
        this.dataScrapperService = dataScrapperService;
    }


    public void prepareForExtract(List<String> entries) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Callable<Void>> tasks = entries.stream()
                    .map(entry -> (Callable<Void>) () -> {
                        dataScrapperService.extractData(entry);
                        return null;
                    })
                    .toList();
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Processing interrupted", e);
        }
    }
}
