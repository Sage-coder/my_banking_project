package com.rab3tech.customer.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.rab3tech.customer.dao.repository.CustomerAddressRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.dao.entity.CustomerAddress;
import com.rab3tech.dao.entity.Login;

public class CustomerAddressServiceImpl implements CustomerAddressService{
	
	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private CustomerAddressRepository customerAddressRepository;
	
	@Override
	public boolean checkAddressPresentOrNot(String username) {
		
		Login  login=loginRepository.findByLoginid(username).get();
		Optional<CustomerAddress> customerAddress=customerAddressRepository.findByUserId(login);
		
		if(customerAddress.equals(login)) {
			return false;
		}
		//CustomerAddress customerAddress=customerAddressRepository.findByUserId(login);
		else {
			return true;
		}
		
		
	}
}
