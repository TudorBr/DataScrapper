package com.company.data.scrapper.service;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

@Service
public class ThreadService {

    private final DataScrapperService dataScrapperService;
    private final DocumentService documentService;
    private static final Logger logger = LoggerFactory.getLogger(ThreadService.class);

    public ThreadService(DataScrapperService dataScrapperService, DocumentService documentService) {
        this.dataScrapperService = dataScrapperService;
        this.documentService = documentService;
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

    public void searchForPatterns() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

        Future<?> future = executor.submit(() -> {
            Document doc;
            while ((doc = dataScrapperService.getDocs().poll()) != null) {
                documentService.searchDocument(doc);
            }
        });
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}
