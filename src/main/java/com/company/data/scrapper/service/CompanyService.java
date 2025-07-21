package com.company.data.scrapper.service;

import com.company.data.scrapper.model.Company;
import com.company.data.scrapper.model.CompanyDTO;
import com.company.data.scrapper.repository.DataScrapperRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final DataScrapperRepository dataScrapperRepository;

    public CompanyService(DataScrapperRepository dataScrapperRepository) {
        this.dataScrapperRepository = dataScrapperRepository;
    }
    public void addNewCompanyData(CompanyDTO companyDTO){ dataScrapperRepository.save(new Company(null, companyDTO.getDomain()
            , companyDTO.getCommercialName()
            , companyDTO.getLegalName()
            , companyDTO.getAvailableNames()
            , companyDTO.getPhone()
            , companyDTO.getFacebook()
            , companyDTO.getAddress()
            , companyDTO.getLocation())); }


}
