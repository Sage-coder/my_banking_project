package com.rab3tech.admin.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rab3tech.dao.entity.PayeeInfo;

public interface PayeeInfoRepository extends JpaRepository<PayeeInfo,Integer>{
	
	//@Query("SELECT t FROM PayeeInfo where t.customerId:puserid")
	Optional <PayeeInfo> findByCustomerIdAndPayeeName(String customerId, String payeeName);
	

	//@Query("SELECT t FROM PayeeInfo where t.payeeNickName:pnickName")
	Optional <PayeeInfo> findByPayeeNickNameAndCustomerId( String payeeNickName, String customerId);
	
	//@Query("SELECT t FROM PayeeInfo where t.payeeAccountNo:pAccountNumber")
	Optional <PayeeInfo> findByPayeeAccountNoAndCustomerId(String payeeAccountNo, String customerId);
	
	List<PayeeInfo> findByCustomerId(String customerId);
	
}
