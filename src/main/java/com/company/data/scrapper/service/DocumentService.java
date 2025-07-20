package com.company.data.scrapper.service;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DocumentService {

    private final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private final Pattern phonePattern = Pattern.compile(
            "\\(?([2-9]\\d{2})\\)?[\\s.-]?([2-9]\\d{2})[\\s.-]?\\d{4}"
    );

    private final Pattern addressPattern = Pattern.compile(
            "\\b(\\d{1,5}\\s+([A-Za-z0-9]+\\s)*(Street|St|Avenue|Ave|Boulevard|Blvd|Road|Rd|Lane|Ln|Drive|Dr|Court|Ct|Circle|Cir|Way|Parkway|Pkwy|Terrace|Ter|Place|Pl|Trail|Trl|Square|Sq|Loop|Highway|Hwy)" +
                    "|(PO Box|P\\.O\\. Box|Post Office Box)\\s*\\d{1,10})\\b\\.?\n"
    );

    Pattern legalName = Pattern.compile(
            "(?i)\"(?:siteName|companyName)\"\\s*:\\s*\"([^\"]+)\"" +
                    "|<meta[^>]*property=\"og:(?:site_name|title)\"[^>]*content=\"([^\"]+)\""
    );


    private final Pattern commercialName = Pattern.compile("\"(companyName)\"\\s*:\\s*\"([^\"]+)\"");


    public Set<String> getPhoneNumber(String content) {
        Set<String> phoneNumbers = new HashSet<>();
        Matcher phoneMatch = phonePattern.matcher(content);
        while (phoneMatch.find()){
            phoneNumbers.add(phoneMatch.group());
        }
        return phoneNumbers;
    }

    public List<String> getAllAddress(String content) {
        List<String> allAddress = new ArrayList<>();
        Matcher addressMatch = addressPattern.matcher(content);
        while (addressMatch.find()){
            allAddress.add(addressMatch.group());
        }
        return allAddress;
    }

    public String getCompanyName(String content) {
       String companyName = null;
        Matcher companyNameMatch = commercialName.matcher(content);
        while (companyNameMatch.find()){
            companyName = companyNameMatch.group();
        }
        return companyName;
    }

    public Set<String>  getLegalName(String content) {
        Set<String> companyLegalNames = new HashSet<>();
        Matcher legalNameMatcher = legalName.matcher(content);
        while (legalNameMatcher.find()){
            companyLegalNames.add(legalNameMatcher.group());
        }
        return companyLegalNames;
    }

    public String getWebSite(String url) {
        String webSite = null;
        if (url.contains("www.")) {
            webSite = StringUtils.substringAfter(url, ".");
        }  else if (url.contains("http:")) {
            webSite = StringUtils.substringAfter(url, "//");
        } else {
            return url;
        }
       return webSite;
    }

    public void searchDocument(Document document) {
        Set<String> allPhones = new HashSet<>();
        Set<String> allLegalNames = new HashSet<>();
        List<String> allAddresses = new ArrayList<>();
        List<String> allCompanyNames = new ArrayList<>();

        String webSite = getWebSite(document.location());

        for (Element script : document.select("script")) {
            String data = script.data();
            if (!StringUtils.isNotBlank(data)) {
                return;
            }
            allPhones.addAll(getPhoneNumber(data));
            allLegalNames.addAll(getLegalName(data));
            allAddresses.addAll(getAllAddress(data));

            String company = getCompanyName(data);
        }

        logger.info("companyNames = {}, legalNames = {}, phones = {}, addresses = {}, website = {}",
                allCompanyNames, allLegalNames, allPhones, allAddresses, webSite);

    }



}
