package com.rab3tech.customer.service.impl;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.rab3tech.aop.advice.TimeLogger;
import com.rab3tech.customer.dao.repository.CustomerAddressRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.dao.entity.AccountStatus;
import com.rab3tech.dao.entity.AccountType;
import com.rab3tech.dao.entity.CustomerAddress;
import com.rab3tech.dao.entity.CustomerSaving;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.service.exception.BankServiceException;
import com.rab3tech.utils.Utils;
import com.rab3tech.vo.CustomerAddressVO;
import com.rab3tech.vo.CustomerSavingVO;
import com.rab3tech.vo.EmailVO;
import com.rab3tech.vo.PayeeInfoVO;

@Service
@Transactional
public class CustomerAddressServiceImpl implements CustomerAddressService{
	
	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private CustomerAddressRepository customerAddressRepository;
	
	@Override
	public boolean checkAddressPresentOrNot(String username) {
		
		Optional <Login>  optional=loginRepository.findByLoginid(username);
		String optionalLogin=optional.get().getLoginid();
		Optional<CustomerAddress> customerAddress=customerAddressRepository.findByUserId(optional.get());
		if(customerAddress.isPresent()) {
			String vos=customerAddress.get().getUserId().getLoginid();
			if(vos.equals(optionalLogin)) {
				
				return true;
			}
			else  {
				return false;
			}
		}
		else {
			return false;
		}
	
	}
	
	///check
	/*
	@Override
	@TimeLogger
	public CustomerAddressVO save(CustomerAddressVO customerAddressVO) {
		customerSavingVO.setDoa(new Date());
		customerSavingVO.setAppref("AS-" + Utils.genRandomAlphaNum());
		boolean b = TransactionSynchronizationManager.isActualTransactionActive();
		if (b) {
			System.out.println("Ahahahahha tx is working!!!!!!!!!!!!!!");
		}

		CustomerSaving entity = new CustomerSaving();
		BeanUtils.copyProperties(customerSavingVO, entity, new String[] { "accType", "status" });
		Optional<AccountType> oaccType = accountTypeRepository.findByName(customerSavingVO.getAccType());
		if (oaccType.isPresent()) {
			entity.setAccType(oaccType.get());
		} else {
			throw new BankServiceException("Hey this " + customerSavingVO.getAccType() + " account type is not valid!");
		}

		AccountStatus accountStatus = new AccountStatus();
		accountStatus.setId(1);
		entity.setStatus(accountStatus);

		CustomerSaving savingEntity = customerAccountEnquiryRepository.save(entity);

		customerSavingVO.setCsaid(savingEntity.getCsaid());

		System.out.println("Email sending .." + LocalDateTime.now());
		emailService.sendEquiryEmail(new EmailVO(customerSavingVO.getEmail(), fromEmail, null,
				"Hello! your account enquiry is submitted successfully.", customerSavingVO.getName()));
		System.out.println("Email done .." + LocalDateTime.now());

		return customerSavingVO;
	}

	*/
	
	@Override
	public Optional <CustomerAddressVO> saveAddressPresentOrNot(String username) {
		
		Optional <Login>  optional=loginRepository.findByLoginid(username);
		String optionalLogin=optional.get().getLoginid();
		CustomerAddressVO address=new CustomerAddressVO();
		
		Optional<CustomerAddress> customerAddress=customerAddressRepository.findByUserId(optional.get());
		if(customerAddress.isPresent()) {
			String vos=customerAddress.get().getUserId().getLoginid();
			if(vos.equals(optionalLogin)) {
				
				address.setAddress_line_1(customerAddress.get().getAddress_line_1());
				address.setAddress_line_2(customerAddress.get().getAddress_line_2());
				address.setAddressId(customerAddress.get().getAddressId());
				address.setCity(customerAddress.get().getCity());
				address.setCountry(customerAddress.get().getCountry());
				address.setFirst_name(customerAddress.get().getFirst_name());
				address.setLast_name(customerAddress.get().getLast_name());
				address.setMobile_number(customerAddress.get().getMobile_number());
				address.setState(customerAddress.get().getState());
				address.setUserId(customerAddress.get().getUserId().getLoginid());
				address.setZipcode(customerAddress.get().getZipcode());
			}
			else  {
				return Optional.empty();
			}
		}
		else {
			return Optional.empty();
		}
		return Optional.of(address);
	}
	@Override
	public String saveCustomerAddress(CustomerAddressVO customerAddressVO) {
		String message=null;
		Optional<Login>  optional=loginRepository.findByLoginid(customerAddressVO.getUserId());
		CustomerAddress customerAddress=new CustomerAddress();
		BeanUtils.copyProperties(customerAddressVO, customerAddress);
		customerAddress.setUserId(optional.get());
		customerAddressRepository.save(customerAddress);
		
		return message;
		
	}
	
	@Override
	public CustomerAddress findCustomerAddress(CustomerAddress customerAddress) {
		Optional<Login>  optional=loginRepository.findByLoginid(customerAddress.getUserId().getEmail());
		CustomerAddress Address=new CustomerAddress();
		BeanUtils.copyProperties(customerAddress,Address);
		return Address;
	}
	
	@Override
	public CustomerAddressVO findByUserId(String userId) {
		Optional <Login>  optional=loginRepository.findByLoginid(userId);
		String optionalLogin=optional.get().getLoginid();
		Optional<CustomerAddress>  customerAddress=customerAddressRepository.findByUserId(optional.get());
		
		CustomerAddressVO Address=new CustomerAddressVO();
		
		BeanUtils.copyProperties(customerAddress,Address);
		return Address;
	}
	
	
	
	@Override
	public void persist(CustomerAddressVO customerAddressVO) {
		Optional<Login>  optional=loginRepository.findByLoginid(customerAddressVO.getUserId());
		CustomerAddress customerAddress= customerAddressRepository.findByAddressId(customerAddressVO.getAddressId());
		//CustomerAddress customerAddress= customerAddressRepository.findByAddressId(customerAddressVO.getAddressId()).get();
		customerAddress.setFirst_name(customerAddressVO.getFirst_name());
		customerAddress.setLast_name(customerAddressVO.getLast_name());
		//customerAddress.setMobile_number(customerAddressVO.getMobile_number());
		customerAddress.setAddress_line_1(customerAddressVO.getAddress_line_1());
		customerAddress.setAddress_line_2(customerAddressVO.getAddress_line_2());
		customerAddress.setCity(customerAddressVO.getCity());
		customerAddress.setState(customerAddressVO.getState());
		customerAddress.setZipcode(customerAddressVO.getZipcode());
		customerAddress.setCountry(customerAddressVO.getCountry());
		customerAddressRepository.save(customerAddress);
		
	}
	
	@Override
	public void deleteCustomerAddress(int addressId) {
		customerAddressRepository.deleteByAddressId(addressId);
	}


	

	
}
