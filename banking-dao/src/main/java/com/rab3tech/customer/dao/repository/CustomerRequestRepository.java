package com.rab3tech.customer.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rab3tech.dao.entity.CustomerRequest;

public interface CustomerRequestRepository extends JpaRepository<CustomerRequest,Integer>{
	Optional <CustomerRequest> findByEmail(String email);
}
