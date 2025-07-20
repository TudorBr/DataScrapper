package com.company.data.scrapper.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {
    private final List<String> csvRecords = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(CsvService.class);

    public void readCsvRecords() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/CSV/sample-websites.csv")))) {
            String entry;
            while ((entry = br.readLine()) != null) {
                csvRecords.add(entry);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.info("csvRecords size: " + csvRecords.size());
    }

    public List<String> getCsvRecords() {
        logger.info("returneaza records");
        return csvRecords;
    }
}
