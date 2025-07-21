package com.company.data.api.service;

import com.company.data.scrapper.DataScrapperApplication;
import com.company.data.scrapper.api.service.RequestService;
import com.company.data.scrapper.service.CsvService;
import com.company.data.util.CsvUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DataScrapperApplication.class)
public class RequestServiceTests {

    @Autowired
    private RequestService requestService;
    @Autowired
    CsvService csvService;

    @Test
    void findByBestMatchTest(){
        String file = RequestServiceTests.class.getResource("/CSV/API-input-sample.csv").toString();
        csvService.readCsvRecords(file);
    String row = CsvUtil.getRandomRow(csvService.getCsvRecords());
    requestService.findBestMatch(row);
    }
}
