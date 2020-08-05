package com.rab3tech.customer.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.rab3tech.admin.dao.repository.AccountStatusRepository;
import com.rab3tech.aop.advice.TimeLogger;
import com.rab3tech.customer.dao.repository.CustomerAddressRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.CustomerRequestRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.customer.dao.repository.RequestTypeRepository;
import com.rab3tech.customer.service.CustomerRequestService;
import com.rab3tech.dao.entity.AccountStatus;
import com.rab3tech.dao.entity.AccountType;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAddress;
import com.rab3tech.dao.entity.CustomerRequest;
import com.rab3tech.dao.entity.CustomerSaving;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.RequestType;
import com.rab3tech.email.service.EmailService;
import com.rab3tech.service.exception.BankServiceException;
import com.rab3tech.utils.Utils;
import com.rab3tech.vo.CustomerRequestVO;
import com.rab3tech.vo.CustomerSavingVO;
import com.rab3tech.vo.EmailVO;
import com.rab3tech.vo.RequestTypeVO;

@Service
@Transactional
public class CustomerRequestServiceImpl implements CustomerRequestService{

	@Autowired
	private RequestTypeRepository requestTypeRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerAddressRepository customerAddressRepository;
	
	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private AccountStatusRepository accountStatusRepository;
	
	@Autowired
	private CustomerRequestRepository customerRequestRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Override
	public List<RequestTypeVO> findAllRequests() {
		List <RequestTypeVO> vo=new ArrayList<>();
		List<RequestType> requestType=requestTypeRepository.findAll();
		for(RequestType requestTypes: requestType) {
			if(requestTypes.getStatus() ==1) {
				RequestTypeVO vos=new RequestTypeVO();
				BeanUtils.copyProperties(requestTypes, vos);
				vo.add(vos);
		}
		}		
		return vo;
		
	}
	
	@Value("${bank.from.email:nagen@gmail.com}")
	private String fromEmail;
	
	@Override
	public String submitRequestByEmail(String userId) {
		
		CustomerRequest customerRequest=new CustomerRequest();
		//customerRequest.setId(id);
		customerRequest.setReqRefNumber("CR-" + Utils.genRandomAlphaNum());
		Customer customer=customerRepository.findByEmail(userId).get();
		customerRequest.setCustomerId(customer);
		customerRequest.setDoa(new Date());
		RequestType requestType=requestTypeRepository.findByName("CHEQUE");
		customerRequest.setRequestType(requestType);
		Optional<Login> login =loginRepository.findByLoginid(userId);
		String optionalLogin=login.get().getLoginid();
		Optional<CustomerAddress> customerAddress=customerAddressRepository.findByUserId(login.get());
		customerRequest.setAddressId(customerAddress.get());
		RequestType requestTypeStatus=requestTypeRepository.findByNameAndStatus("CHEQUE", 1);
		customerRequest.setStatus(requestTypeStatus);
		customerRequest.setEmail(userId);
		customerRequestRepository.save(customerRequest);
		
		//String name, String to, String from, String subject, String body, String refNumber
		System.out.println("Email sending .." + LocalDateTime.now());
		emailService.sendRequestEmail(new EmailVO(customer.getName(),customer.getEmail(), fromEmail, "Regarding your loan request",
				"Hello! your loan request is submitted successfully.", customerRequest.getReqRefNumber()));
		System.out.println("Email done .." + LocalDateTime.now());

		return customerRequest.getReqRefNumber();
	}
	
	@Override
	@TimeLogger
	public boolean emailNotExist(String email) {
		Optional<CustomerRequest> optional = customerRequestRepository.findByEmail(email);
		if(optional.isPresent()) {
			return false;
		}else {
			return true;	
		}
	}
	

	@Override
	public RequestTypeVO findById(int id) {
		RequestTypeVO vo=new RequestTypeVO();
		RequestType requestType=requestTypeRepository.findById(id).get();
		BeanUtils.copyProperties(requestType, vo);
		return vo;
	}

}
