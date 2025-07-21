package com.company.data.scrapper.service;

import com.company.data.scrapper.model.CompanyDTO;
import com.company.data.util.FilterSimilarNames;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

    private CompanyDTO companyDTO;
    private final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private final Pattern phonePattern = Pattern.compile(
            "\\(?([2-9]\\d{2})\\)?[\\s.-]?([2-9]\\d{2})[\\s.-]?\\d{4}"
    );

    private final Pattern addressPattern = Pattern.compile(
            "\\b(\\d{1,5}\\s+([A-Za-z0-9]+\\s)*" +
                    "(Street|St|Avenue|Ave|Boulevard|Blvd|Road|Rd|Lane|Ln|Drive|Dr|Court|Ct|Circle|Cir|Way|" +
                    "Parkway|Pkwy|Terrace|Ter|Place|Pl|Trail|Trl|Square|Sq|Loop|Highway|Hwy))\\b" +
                    "|\\b(PO Box|P\\.O\\. Box|Post Office Box)\\s*\\d{1,10}\\b" +
                    "|\\b\\d{1,5}\\s+([A-Za-z]+\\s?)+,?\\s*([A-Za-z]+),?\\s*(\\w{2})\\s*(\\d{5})?\\b",
            Pattern.CASE_INSENSITIVE
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

    public List<String> getScriptAddress(String content) {
        List<String> allAddress = new ArrayList<>();
        Matcher addressMatch = addressPattern.matcher(content);
        while (addressMatch.find()){
            allAddress.add(addressMatch.group());
        }
        return allAddress;
    }

    public  List<String> getMetaDataAddress(Document document) {
        List<String> allAddress = new ArrayList<>();
        Elements metaTags = document.select("meta[property*=location], meta[itemprop*=address]");
        for (Element meta : metaTags) {
            allAddress.add(meta.attr("content"));
        }
        Elements locationTags = document.select("[itemprop=address], .address, .location, .contact-location");
        for (Element tag : locationTags) {
            allAddress.add(tag.text());
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
        while (legalNameMatcher.find()) {
            String matchedName = legalNameMatcher.group(1) != null
                    ? legalNameMatcher.group(1)
                    : legalNameMatcher.group(2);

            if (matchedName != null && !matchedName.isBlank()) {
                companyLegalNames.add(matchedName.trim());
            }
        }
        return companyLegalNames;
    }

    public String getWebSite(String url) {
        String webSite = null;
        if (url.contains("www.")) {
            webSite = StringUtils.substringAfter(url, ".");
        }  else if (url.contains("https://")) {
            webSite = StringUtils.substringAfter(url, "//");
        } else {
            return url;
        }
        return webSite;
    }

    private String extractFacebookLink(Document doc) {
        return doc.select("a[href]")
                .stream()
                .map(link -> link.attr("abs:href").toLowerCase())
                .filter(href -> href.contains("facebook.com") &&
                        !href.contains("share") &&
                        !href.contains("sharer") &&
                        !href.contains("dialog") &&
                        !href.contains("plugins"))
                .findFirst()
                .orElse(null);
    }

    public List<String>  getAllCompanyNames(Document doc) {
        List<String>  companyNames = new ArrayList<>();

        Elements metaTags = doc.select("meta[property=og:site_name], meta[property=og:title]");
        for (Element tag : metaTags) {
            String name = tag.attr("content").trim();
            if (!name.isEmpty()) companyNames.add(name);
        }

        String title = doc.title();
        if (title != null && !title.isBlank()) companyNames.add(title.trim());


        Elements scripts = doc.select("script[type=application/ld+json]");
        for (Element script : scripts) {
            String json = script.html();
            Matcher matcher = Pattern.compile("\"(name|legalName|alternateName)\"\\s*:\\s*\"([^\"]+)\"")
                    .matcher(json);
            while (matcher.find()) {
                String name = matcher.group(2).trim();
                if (!name.isEmpty()) companyNames.add(name);
            }
        }

        Elements logoAlts = doc.select("img[alt]");
        for (Element img : logoAlts) {
            String alt = img.attr("alt").trim();
            if (alt.length() >= 3) companyNames.add(alt); // Avoid generic alt tags
        }

        return FilterSimilarNames.filterAllNames(companyNames, 0.4);

    }



    public CompanyDTO searchDocument(Document document) {
        companyDTO = new CompanyDTO();

        companyDTO.setDomain(getWebSite(document.location()));
        companyDTO.setFacebook(extractFacebookLink(document));
        companyDTO.getAddress().addAll(getMetaDataAddress(document));
        companyDTO.getAvailableNames().addAll(getAllCompanyNames(document));
        searchForScripts(document);

        logger.info("CompanyDTO: {}", companyDTO);

        return companyDTO;
    }

    public void searchForScripts(Document document) {
        for (Element script : document.select("script")) {
            String data = script.data();
            if (StringUtils.isNotBlank(data)) {
                companyDTO.getPhone().addAll(getPhoneNumber(data));
                companyDTO.getLegalName().addAll(getLegalName(data));
                companyDTO.getAddress().addAll(getScriptAddress(data));
                companyDTO.setCommercialName(getCompanyName(data));
            }
        }
    }

}
