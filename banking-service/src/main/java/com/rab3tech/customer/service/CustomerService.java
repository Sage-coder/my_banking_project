package com.rab3tech.customer.service;

import java.util.List;
import java.util.Optional;

import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.PayeeInfoVO;

public interface CustomerService {

	CustomerVO createAccount(CustomerVO customerVO);

	
	CustomerVO findByEmail(String username);


	void updateCustomer(CustomerVO customerVO);

	

	CustomerAccountInfoVO createBankAccount(int csaid);


	Optional<CustomerAccountInfoVO> findByUserId(String customerId);

	List<CustomerAccountInfoVO> findListByUserId(String customerId);
	
}
