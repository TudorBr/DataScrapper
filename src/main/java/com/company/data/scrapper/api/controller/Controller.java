package com.company.data.scrapper.api.controller;


import com.company.data.scrapper.api.service.RequestService;
import com.company.data.scrapper.model.CompanyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/query")
@ResponseStatus
public class Controller {

    private final RequestService requestService;

    public Controller(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/match")
    public ResponseEntity<?> handleCsvRequest(@RequestParam("row") String row){
        if (!StringUtils.hasText(row)) {
            return ResponseEntity.badRequest().body("Request is empty");
        }
        CompanyDTO companyDTO = requestService.findBestMatch(row);
        if (companyDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(companyDTO);
    }
}
