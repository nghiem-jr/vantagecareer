package com.nghiemdd.vantagecareer.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nghiemdd.vantagecareer.domain.Company;
import com.nghiemdd.vantagecareer.domain.dto.ResultPaginationDTO;
import com.nghiemdd.vantagecareer.service.CompanyService;
import com.nghiemdd.vantagecareer.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<?> createNewCompany(@Valid @RequestBody Company postmanCompany) {
        Company newCompany = this.companyService.handleCreateCompany(postmanCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @GetMapping("/companies")
    @ApiMessage("fetch all companies")
    public ResponseEntity<ResultPaginationDTO> getCompanies(@Filter Specification<Company> spec, Pageable pageable) {

        ResultPaginationDTO companies = this.companyService.fetchAllCompanies(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(companies);
    }
    // @GetMapping("/companies")
    // public ResponseEntity<ResultPaginationDTO>
    // getCompanies(@RequestParam("current") Optional<String> currentOptional,
    // @RequestParam("pageSize") Optional<String> pageSizeOptional) {

    // String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
    // String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() :
    // "";
    // Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1,
    // Integer.parseInt(sPageSize));
    // ResultPaginationDTO companies =
    // this.companyService.fetchAllCompanies(pageable);
    // return ResponseEntity.status(HttpStatus.OK).body(companies);
    // }

    @PutMapping("companies")
    public ResponseEntity<Company> updateCompany(@RequestBody Company company) {
        Company updatedCompany = this.companyService.handleUpdateCompany(company);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCompany);
    }

    @DeleteMapping("companies/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("companyId") Long companyId) {
        this.companyService.handleDeleteCompany(companyId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}