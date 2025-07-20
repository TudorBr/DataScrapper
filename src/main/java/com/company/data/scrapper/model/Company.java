package com.company.data.scrapper.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;


@Entity
public record Company(@Id Long id, String domain,
                      String commercialName, String legalName,
                      List<String> availableNames, String phone, String facebook,
                      String address, String location) {
}
