package com.rab3tech.customer.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.CustomerSaving;
import com.rab3tech.dao.entity.Login;

public interface CustomerAccountInfoRepository extends JpaRepository<CustomerAccountInfo, Long>{
	CustomerAccountInfo findByCustomerId(String customerId);
	
	@Query("SELECT t FROM CustomerAccountInfo t where t.customerId=  :puserid")
	Optional <CustomerAccountInfo> findAccountByEmail(@Param("puserid" ) Login customerId);
	
	@Query("SELECT t FROM CustomerAccountInfo t where t.accountNumber=  :payeeAccountNumber")
	Optional <CustomerAccountInfo> findAccountByAccountNumber(@Param("payeeAccountNumber" ) String accountNumber);
	
	@Query("SELECT t FROM CustomerAccountInfo t where t.customerId=  :puserid")
	List<CustomerAccountInfo> findByEmail(@Param("puserid" )Login customerId);

}
