package com.nghiemdd.vantagecareer.repository;

import org.springframework.stereotype.Repository;

import com.nghiemdd.vantagecareer.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

}
