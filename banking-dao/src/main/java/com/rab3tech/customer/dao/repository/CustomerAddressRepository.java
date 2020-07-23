package com.rab3tech.customer.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rab3tech.dao.entity.CustomerAddress;
import com.rab3tech.dao.entity.Login;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Integer>{
	Optional <CustomerAddress> findByUserId(Login loginid);

}
