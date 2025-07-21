package com.company.data.scrapper.api.service;

import com.company.data.scrapper.model.Company;
import com.company.data.scrapper.model.CompanyDTO;
import com.company.data.scrapper.repository.DataScrapperRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    private CompanyDTO companyDTO = new CompanyDTO();
    private final DataScrapperRepository dataScrapperRepository;

    public RequestService(DataScrapperRepository dataScrapperRepository) {
        this.dataScrapperRepository = dataScrapperRepository;
    }

    public CompanyDTO findBestMatch(String row) {
    String[] rowValues = row.split(",", -1);

    String inputName = rowValues[0];
    String inputPhone = rowValues[1];
    String inputWebsite = rowValues[2];
    String inputFacebook = rowValues[3];

        if (StringUtils.isNotBlank(inputName) &&
                StringUtils.isBlank(inputPhone) &&
                StringUtils.isBlank(inputWebsite) &&
                StringUtils.isBlank(inputFacebook)) {
            return findByName(inputName);
        } else if (StringUtils.isBlank(inputName) &&
                StringUtils.isNotBlank(inputPhone) &&
                StringUtils.isBlank(inputWebsite) &&
                StringUtils.isBlank(inputFacebook)) {
            return findByPhone(inputPhone);
        } else if (StringUtils.isBlank(inputName) &&
                StringUtils.isBlank(inputPhone) &&
                StringUtils.isNotBlank(inputWebsite) &&
                StringUtils.isBlank(inputFacebook)) {
            return findByDomain(inputWebsite);
        } else if (StringUtils.isBlank(inputName) &&
                StringUtils.isBlank(inputPhone) &&
                StringUtils.isBlank(inputWebsite) &&
                StringUtils.isNotBlank(inputFacebook)) {
            return findByFacebook(inputFacebook);
        } else {
            companyDTO.getPhone().add(inputPhone);
            companyDTO.setFacebook(inputFacebook);
            companyDTO.setDomain(inputWebsite);
            return searchCompanyByDTO(companyDTO, inputName);
        }
    }

    public CompanyDTO findByName(String inputName){
        Optional<Company> optionalCompany = dataScrapperRepository.findByName(inputName);
        if (!optionalCompany.isEmpty()) {
            return companyDTO.fromEntity(optionalCompany.get());
        }
        return  null;
    }
    public CompanyDTO findByPhone(String inputPhone ) {
        Optional<Company> optionalCompany = dataScrapperRepository.findByPhone(inputPhone);
        if (optionalCompany.isPresent()) {
            return companyDTO.fromEntity(optionalCompany.get());
        }
        return  null;
    }
    public CompanyDTO findByDomain(String inputWebsite){
        Optional<Company> optionalCompany = dataScrapperRepository.findByDomain(inputWebsite);
        if (optionalCompany.isPresent()) {
            return companyDTO.fromEntity(optionalCompany.get());
        }
          return  null;
    }
    public CompanyDTO findByFacebook(String inputFacebook ){
        Optional<Company> optionalCompany = dataScrapperRepository.findByFacebook(inputFacebook);
        if (optionalCompany.isPresent()) {
            return companyDTO.fromEntity(optionalCompany.get());
        }
        return  null;
    }

    public CompanyDTO searchCompanyByDTO(CompanyDTO companyDTOValues, String inputName ){
        // pick out one phone or null
        String phone = companyDTOValues.getPhone().stream().findFirst().orElse(null);

        List<Company> matches = dataScrapperRepository.searchAll(
                StringUtils.isNotBlank(inputName)  ? inputName   : null,
                StringUtils.isNotBlank(phone)      ? phone       : null,
                StringUtils.isNotBlank(companyDTOValues.getDomain())   ? companyDTOValues.getDomain()   : null,
                StringUtils.isNotBlank(companyDTOValues.getFacebook()) ? companyDTOValues.getFacebook() : null
        );

        return matches.stream()
                .findFirst()
                .map(companyDTO::fromEntity)
                .orElse(null);
    }

}
