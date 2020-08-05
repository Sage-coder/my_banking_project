package com.rab3tech.customer.service.impl;

import java.util.Optional;

import com.rab3tech.dao.entity.CustomerAddress;
import com.rab3tech.vo.CustomerAddressVO;
import com.rab3tech.vo.PayeeInfoVO;

public interface CustomerAddressService {

	boolean checkAddressPresentOrNot(String username);

	String saveCustomerAddress(CustomerAddressVO customerAddressVO);

	CustomerAddress findCustomerAddress(CustomerAddress customerAddress);

	Optional<CustomerAddressVO> saveAddressPresentOrNot(String username);

	

	void persist(CustomerAddressVO customerAddressVO);


	void deleteCustomerAddress(int addressId);

	CustomerAddressVO findByUserId(String userId);



}
