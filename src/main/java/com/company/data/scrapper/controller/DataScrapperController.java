package com.company.data.scrapper.controller;

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
    private static final Logger logger = LoggerFactory.getLogger(DataScrapperController.class);


    public DataScrapperController(CsvService csvService, ThreadService threadService) {
        this.csvService = csvService;
        this.threadService = threadService;
    }

    private void startScrappingOperation() {
        csvService.readCsvRecords();
        threadService.prepareForExtract(csvService.getCsvRecords());
    }


    @PostConstruct
    void init() {
        startScrappingOperation();
    }
}
