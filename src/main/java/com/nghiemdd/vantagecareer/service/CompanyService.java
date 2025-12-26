package com.nghiemdd.vantagecareer.service;

import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nghiemdd.vantagecareer.domain.Company;
import com.nghiemdd.vantagecareer.domain.User;
import com.nghiemdd.vantagecareer.domain.dto.Meta;
import com.nghiemdd.vantagecareer.domain.dto.ResultPaginationDTO;
import com.nghiemdd.vantagecareer.repository.CompanyRepository;

@Service
public class CompanyService {
    CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    // public List<Company> fetchAllCompanies(Pageable pageable) {
    // Page<Company> page = this.companyRepository.findAll(pageable);
    // return page.getContent();
    // }

    public ResultPaginationDTO fetchAllCompanies(Pageable pageable) {
        Page<Company> page = this.companyRepository.findAll(pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(page.getNumber() + 1);
        mt.setPageSize(page.getSize());

        mt.setTotal(page.getTotalElements());
        mt.setPages(page.getTotalPages());

        rs.setMeta(mt);
        rs.setResult(page.getContent());

        return rs;
    }

    public Company handleUpdateCompany(Company company) {

        Company currentCompany = this.companyRepository.findById(company.getId()).orElse(null);
        if (currentCompany != null) {
            currentCompany.setName(company.getName());
            currentCompany.setDescription(company.getDescription());
            currentCompany.setAddress(company.getAddress());
            currentCompany.setLogo(company.getLogo());
            return this.companyRepository.save(currentCompany);
        } else {
            return null;
        }
    }

    public void handleDeleteCompany(Long companyId) {
        this.companyRepository.deleteById(companyId);
    }
}
