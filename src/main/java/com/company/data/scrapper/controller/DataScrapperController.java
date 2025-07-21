package com.company.data.scrapper.controller;

import com.company.data.scrapper.analytics.ErrorTracker;
import com.company.data.scrapper.service.CsvService;
import com.company.data.scrapper.service.ThreadService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class DataScrapperController {

    private final CsvService csvService;
    private final ThreadService threadService;
    private final ErrorTracker errorTracker;
    private static final Logger logger = LoggerFactory.getLogger(DataScrapperController.class);


    public DataScrapperController(CsvService csvService, ThreadService threadService, ErrorTracker errorTracker) {
        this.csvService = csvService;
        this.threadService = threadService;
        this.errorTracker = errorTracker;
    }

    private void startScrappingOperation() {
        csvService.readCsvRecords(CsvService.CSV_FILE_PATH);
        threadService.prepareForExtract(csvService.getCsvRecords());
        threadService.searchForPatterns();
     //   errorTracker.printErrorCounts();

    }


    @PostConstruct
    void init() {

        startScrappingOperation();
    }
}
