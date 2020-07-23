package com.rab3tech.customer.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerSaving;

/**
 * @author nagendra
 * Spring Data JPA repository
 *
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	public Optional<Customer> findByEmail(String email);
	
	@Query("SELECT tt FROM CustomerSaving tt where tt.status.name = :name") 
	List<CustomerSaving> findPendingEnquiries(@Param("name") String name);
	
	
}

