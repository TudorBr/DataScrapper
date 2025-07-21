package com.company.data.scrapper.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "company")
public class Company {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String domain;

        private String commercialName;

        @ElementCollection
        @CollectionTable(
                name = "company_legal_names",
                joinColumns = @JoinColumn(name = "company_id")
        )
        @Column(name = "legal_name")
        private Set<String> legalName = new HashSet<>();

        @ElementCollection
        @CollectionTable(
                name = "company_available_names",
                joinColumns = @JoinColumn(name = "company_id")
        )
        @Column(name = "available_name", length = 1024)
        private List<String> availableNames = new ArrayList<>();

        @ElementCollection
        @CollectionTable(
                name = "company_phones",
                joinColumns = @JoinColumn(name = "company_id")
        )
        @Column(name = "phone", length = 1024)
        private Set<String> phone = new HashSet<>();
        @Column(name = "facebook", length = 1024)
        private String facebook;

        @ElementCollection
        @CollectionTable(
                name = "company_addresses",
                joinColumns = @JoinColumn(name = "company_id")
        )
        @OrderColumn(name = "address_idx")
        @Column(name = "address", length = 1024)
        private List<String> address = new ArrayList<>();

        private String location;

        // Required by JPA
        public Company() {
        }

        // Convenience constructor
        public Company(Long id,
                       String domain,
                       String commercialName,
                       Set<String> legalName,
                       List<String> availableNames,
                       Set<String> phone,
                       String facebook,
                       List<String> address,
                       String location) {
                this.id = id;
                this.domain = domain;
                this.commercialName = commercialName;
                this.legalName = legalName != null ? legalName : new HashSet<>();
                this.availableNames = availableNames != null ? availableNames : new ArrayList<>();
                this.phone = phone != null ? phone : new HashSet<>();
                this.facebook = facebook;
                this.address = address != null ? address : new ArrayList<>();
                this.location = location;
        }

        // Getters & setters

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
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
}
