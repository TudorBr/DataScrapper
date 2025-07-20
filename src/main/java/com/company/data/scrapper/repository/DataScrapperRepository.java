package com.company.data.scrapper.repository;

import com.company.data.scrapper.model.Company;
import org.springframework.data.repository.CrudRepository;

public interface DataScrapperRepository extends CrudRepository<Company, Long> {
}
