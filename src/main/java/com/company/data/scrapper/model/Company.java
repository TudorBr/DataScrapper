package com.company.data.scrapper.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;
import java.util.Set;


@Entity
public record Company(@Id Long id, String domain,
                      String commercialName, Set<String> legalName,
                      List<String> availableNames, Set<String> phone, String facebook,
                      List<String> address, String location) {
}
