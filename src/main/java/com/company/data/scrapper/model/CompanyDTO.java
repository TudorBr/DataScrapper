package com.company.data.scrapper.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompanyDTO {
    private String domain;
    private String commercialName;
    private Set<String> legalName = new HashSet<>();
    private List<String> availableNames = new ArrayList<>();
    private Set<String> phone = new HashSet<>();
    private String facebook;
    private List<String> address = new ArrayList<>();
    private String location;

    public CompanyDTO() {}

    public CompanyDTO (String domain, String commercialName, Set<String> legalName,
                      List<String> availableNames, Set<String> phone, String facebook,
                      List<String> address, String location) {
        this.domain = domain;
        this.commercialName = commercialName;
        this.legalName = legalName;
        this.availableNames = availableNames;
        this.phone = phone;
        this.facebook = facebook;
        this.address = address;
        this.location = location;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public Set<String> getLegalName() {
        return legalName;
    }

    public void setLegalName(Set<String> legalName) {
        this.legalName = legalName;
    }

    public List<String> getAvailableNames() {
        return availableNames;
    }

    public void setAvailableNames(List<String> availableNames) {
        this.availableNames = availableNames;
    }

    public Set<String> getPhone() {
        return phone;
    }

    public void setPhone(Set<String> phone) {
        this.phone = phone;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isEmpty() {
        return domain == null &&
                commercialName == null &&
                (legalName == null || legalName.isEmpty()) &&
                (availableNames == null || availableNames.isEmpty()) &&
                (phone == null || phone.isEmpty()) &&
                facebook == null &&
                (address == null || address.isEmpty()) &&
                location == null;
    }


    @Override
    public String toString() {
        return "CompanyDTO{" +
                ", domain='" + domain + '\'' +
                ", commercialName='" + commercialName + '\'' +
                ", legalName=" + (legalName == null ? "null" : legalName.toString()) +
                ", availableNames=" + (availableNames == null ? "null" : availableNames.toString()) +
                ", phone=" + (phone == null ? "null" : phone.toString()) +
                ", facebook='" + facebook + '\'' +
                ", address=" + (address == null ? "null" : address.toString()) +
                ", location='" + location + '\'' +
                '}';
    }

    public CompanyDTO fromEntity(Company company) {
        return new CompanyDTO(
                company.getDomain(),
                company.getCommercialName(),
                company.getLegalName(),
                company.getAvailableNames(),
                company.getPhone(),
                company.getFacebook(),
                company.getAddress(),
                company.getLocation()
        );
    }


}
