package com.rab3tech.customer.service.impl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.admin.dao.repository.AccountStatusRepository;
import com.rab3tech.admin.dao.repository.AccountTypeRepository;
import com.rab3tech.admin.dao.repository.CustomerSecurityQuestionsRepository;
import com.rab3tech.admin.dao.repository.MagicCustomerRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountApprovedRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountEnquiryRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerQuestionsAnsRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.RoleRepository;
import com.rab3tech.customer.dao.repository.SecurityQuestionsRepository;
import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.dao.entity.AccountStatus;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.CustomerQuestionAnswer;
import com.rab3tech.dao.entity.CustomerSaving;
import com.rab3tech.dao.entity.CustomerSavingApproved;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.RequestType;
import com.rab3tech.dao.entity.Role;
import com.rab3tech.dao.entity.SecurityQuestions;
import com.rab3tech.utils.AccountStatusEnum;
import com.rab3tech.utils.PasswordGenerator;
import com.rab3tech.utils.Utils;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerSavingVO;
import com.rab3tech.vo.CustomerSecurityQueAnsVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.LoginVO;
import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.RequestTypeVO;

@Service
@Transactional
public class CustomerServiceImpl implements  CustomerService{
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	@Autowired
	private SecurityQuestionService securityQuestionService ;
	
	@Autowired
	private MagicCustomerRepository customerRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private CustomerRepository customerAccRepository;
	
	@Autowired
	private AccountStatusRepository accountStatusRepository;
	
	@Autowired
	private CustomerAccountInfoRepository customerAccountInfoRepository;
	
	
	
	@Autowired
	private CustomerAccountEnquiryRepository customerAccountEnquiryRepository;
	
	@Autowired
	private CustomerQuestionsAnsRepository customerQuestionsAnsRepository;
	
	@Autowired
	private SecurityQuestionsRepository securityQuestionsRepository;
	
	@Autowired
	private CustomerSecurityQuestionsRepository customerSecurityQuestionsRepository;
	
	@Autowired
	private AccountTypeRepository accountTypeRepository;
	
	@Autowired
	private CustomerAccountApprovedRepository customerAccountApprovedRepository;
	
	public String[] ignoreNullData(Object source) {
		BeanWrapper wrapper=new BeanWrapperImpl(source);
		PropertyDescriptor [] pDesc=wrapper.getPropertyDescriptors();
		Set<String> nullValue=new HashSet<String>();
		for(PropertyDescriptor pd:pDesc) {
			Object obj=wrapper.getPropertyValue(pd.getName());
			if(obj==null)
				nullValue.add(pd.getName());
		}
		String[] ignoreData=new String [nullValue.size()];
		return nullValue.toArray(ignoreData);
	}
	
	@Override
	public CustomerAccountInfoVO createBankAccount(int csaid) {
		String customerAccount=Utils.generateCustomerAccount();
		CustomerSaving customerSaving=customerAccountEnquiryRepository.findById(csaid).get();
		CustomerAccountInfo customerAccountInfo=new CustomerAccountInfo();
		customerAccountInfo.setAccountNumber(customerAccount);
		customerAccountInfo.setAccountType(customerSaving.getAccType());
		customerAccountInfo.setAvBalance(1000.0F);
		customerAccountInfo.setBranch(customerSaving.getLocation());
		customerAccountInfo.setCurrency("$");
		//customerAccountInfo.setAccount_status("ACTIVE");
		
		Customer customer=customerRepository.findByEmail(customerSaving.getEmail()).get();
		
		customerAccountInfo.setCustomerId(customer.getLogin());
		customerAccountInfo.setStatusAsOf(new Date());
		customerAccountInfo.setTavBalance(1000.0F);
		CustomerAccountInfo customerAccountInfoVO2=customerAccountInfoRepository.save(customerAccountInfo);
		CustomerSavingApproved customerSavingApproved=new CustomerSavingApproved();
		BeanUtils.copyProperties(customerSaving, customerSavingApproved);
		customerSavingApproved.setStatus(customerSaving.getStatus());
		customerSavingApproved.setAccType(customerSaving.getAccType());
		//saving entity into customerAccountApproved
		customerAccountApprovedRepository.save(customerSavingApproved);
		//deleting entity into customerAccountEnquiry
		customerAccountEnquiryRepository.delete(customerSaving);
		CustomerAccountInfoVO accountInfoVO=new CustomerAccountInfoVO();
		BeanUtils.copyProperties(customerAccountInfoVO2, accountInfoVO);
		return accountInfoVO;
	}
	
	/*
	 CustomerSaving customerSavingEntity = customerAccountEnquiryRepository.findById(csaid).get();
		CustomerSavingVO customerSavingVO = new CustomerSavingVO();
		BeanUtils.copyProperties(customerSavingEntity, customerSavingVO, new String[] { "accType", "status" });
		customerSavingVO.setAccType(customerSavingEntity.getAccType().getName());
		customerSavingVO.setStatus(customerSavingEntity.getStatus().getName());
		return customerSavingVO;*/
	
	@Override
	public  Optional<CustomerAccountInfoVO> findByUserId(String customerId){
		Customer customer=customerRepository.findByEmail(customerId).get();
		Optional<CustomerAccountInfo> account=customerAccountInfoRepository.findAccountByEmail(customer.getLogin());
		CustomerAccountInfoVO accountVO=new CustomerAccountInfoVO();
		if(account.isPresent()) {
			BeanUtils.copyProperties(account.get(), accountVO);
			LoginVO loginVOs=new LoginVO();
			BeanUtils.copyProperties(account.get().getCustomerId(),loginVOs );
			loginVOs.setUsername(account.get().getCustomerId().getLoginid());
			accountVO.setCustomerId(loginVOs);
			accountVO.setAccountType(account.get().getAccountType());
			//BeanUtils.copyProperties(account.get(), accountVO,new String[]{"customerId", "accountType"});
			//accountVO.setAccountType(account.get().getAccountType()!null?account.get().getAccountType().getName():null);
			//accountVO.setAccountType(account.get().getAccountType()!=null?account.get().getAccountType().getCode():null);
		
			return Optional.of(accountVO);
			
		
		}
		else {
			return Optional.empty();
		}
}
	
	@Override
	public  List<CustomerAccountInfoVO> findListByUserId(String customerId){
		Customer customer=customerRepository.findByEmail(customerId).get();
		List<CustomerAccountInfo> account=customerAccountInfoRepository.findByEmail(customer.getLogin());
		List<CustomerAccountInfoVO> accountVO=new ArrayList<>();
		for(CustomerAccountInfo ca:account) {
			CustomerAccountInfoVO vos=new CustomerAccountInfoVO();
			//vos.setCustomerId(customer.getEmail());
			BeanUtils.copyProperties(ca, vos);
			
			accountVO.add(vos);
		}
		
		return accountVO;
}
	
	@Override
	public CustomerVO createAccount(CustomerVO customerVO) {
		Customer pcustomer = new Customer();
		BeanUtils.copyProperties(customerVO, pcustomer);
		Login login = new Login();
		login.setNoOfAttempt(3);
		login.setLoginid(customerVO.getEmail());
		login.setName(customerVO.getName());
		String genPassword=PasswordGenerator.generateRandomPassword(8);
		customerVO.setPassword(genPassword);
		login.setPassword(bCryptPasswordEncoder.encode(genPassword));
		login.setToken(customerVO.getToken());
		login.setLocked("no");
		
		Role entity=roleRepository.findById(3).get();
		Set<Role> roles=new HashSet<>();
		roles.add(entity);
		//setting roles inside login
		login.setRoles(roles);
		//setting login inside
		pcustomer.setLogin(login);
		Customer dcustomer=customerRepository.save(pcustomer);
		customerVO.setId(dcustomer.getId());
		customerVO.setUserid(customerVO.getUserid());
		
		Optional<CustomerSaving> optional=customerAccountEnquiryRepository.findByEmail(dcustomer.getEmail());
		if (optional.isPresent()){
			CustomerSaving customerSaving=optional.get();
			AccountStatus accountStatus =accountStatusRepository.findByCode(AccountStatusEnum.REGISTERED.getCode()).get();
			customerSaving.setStatus(accountStatus);
		}
		return customerVO;
	}
	
	
	@Override
	public CustomerVO findByEmail(String username) {

		Customer customer = customerRepository.findByEmail(username).get();
		CustomerVO customerVO= new CustomerVO();
		BeanUtils.copyProperties(customer,customerVO);
		List<CustomerQuestionAnswer> qAnswer=customerQuestionsAnsRepository.findQuestionAnswer(username);
		//String questionText1=customerSecurityQuestionsRepository.findById(Integer.parseInt(qAnswer.get(0).getQuestion())).get().getQuestions();
		//String questionText2=customerSecurityQuestionsRepository.findById(Integer.parseInt(qAnswer.get(1).getQuestion())).get().getQuestions();
		customerVO.setQuestion1(qAnswer.get(0).getQuestion());
		customerVO.setQuestion2(qAnswer.get(0).getQuestion());
		customerVO.setAnswer1(qAnswer.get(0).getAnswer());
		customerVO.setAnswer2(qAnswer.get(1).getAnswer());
		List<SecurityQuestions> questionList=securityQuestionsRepository.findAll();
		return customerVO;
		}
	
	
	@Override
	public void updateCustomer(CustomerVO customerVO) {
		String methodName="updateCustomer";
		logger.debug(customerVO.toString());
		Customer customer=customerRepository.findByEmail(customerVO.getEmail()).get();
		BeanUtils.copyProperties(customerVO, customer, ignoreNullData(customerVO));
		customerRepository.save(customer);
		
		List<CustomerQuestionAnswer> qAnswer=customerQuestionsAnsRepository.findQuestionAnswer(customerVO.getEmail());
		if(qAnswer.get(0).getAnswer().equals(customerVO.getAnswer1()) && qAnswer.get(1).getAnswer().equals(customerVO.getAnswer2()) &&
				qAnswer.get(0).getQuestion().equals(customerVO.getQuestion1()) && qAnswer.get(1).getQuestion().equals(customerVO.getQuestion2())){
			logger.debug("No changes done is security question section");
		}
		else {
			CustomerSecurityQueAnsVO customerSecurityQuesAnsVO=new CustomerSecurityQueAnsVO();
			customerSecurityQuesAnsVO.setLoginid(customerVO.getEmail());
			customerSecurityQuesAnsVO.setSecurityQuestion1(customerVO.getQuestion1());
			customerSecurityQuesAnsVO.setSecurityQuestion2(customerVO.getQuestion2());
			customerSecurityQuesAnsVO.setSecurityQuestionAnswer1(customerVO.getAnswer1());
			customerSecurityQuesAnsVO.setSecurityQuestionAnswer2(customerVO.getAnswer2());
			securityQuestionService.save(customerSecurityQuesAnsVO);
			
		}
		
		
	}

	

	
}
