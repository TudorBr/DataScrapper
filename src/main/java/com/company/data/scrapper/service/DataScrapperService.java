package com.company.data.scrapper.service;

import com.company.data.scrapper.analytics.ErrorTracker;
import com.company.data.scrapper.repository.DataScrapperRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


@Service
public class DataScrapperService {

    private final ErrorTracker errorTracker;
    private final Queue<Document> docs = new ConcurrentLinkedQueue();
    private final Logger logger = LoggerFactory.getLogger(DataScrapperService.class);





    public DataScrapperService(ErrorTracker errorTracker) {
        this.errorTracker = errorTracker;
    }

    public void extractData(String domanin) {

        try {
            Document doc = Jsoup.connect("https://www." + domanin)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .timeout(5000)
                    .get();
            docs.add(doc);

            try{

            }catch (Exception e) {
               logger.error(e.getMessage());
                errorTracker.logError(e.getMessage());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            errorTracker.logError(e.getMessage());
        }
    }


    public Queue<Document> getDocs() {
        return docs;
    }
}
