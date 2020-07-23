package com.rab3tech.customer.service.impl;

import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.rab3tech.admin.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.Transaction;
import com.rab3tech.customer.dao.repository.TransactionRepository;
import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.TransactionVO;

@Transactional
@Service
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private PayeeInfoRepository payeeInfoRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerAccountInfoRepository customerAccountInfoRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Override
	public String transferFund(@ModelAttribute TransactionVO transactionVO) {
		String message=null;
		
		CustomerAccountInfo ownAccount=new CustomerAccountInfo();
		
		Customer customer=customerRepository.findByEmail(transactionVO.getCustomerId()).get();
		Optional<CustomerAccountInfo> account=customerAccountInfoRepository.findAccountByEmail(customer.getLogin());

		//checking customer account number is valid (FROM account number)
		if(account.isPresent()) {
			 ownAccount=account.get();
			/*if(!ownAccount.getAccount_status().equalsIgnoreCase("ACTIVE")) {
				message="You is not an ACTIVE account";
				return message;
			}*/
			//checking if the customer account number is saving
			if(!ownAccount.getAccountType().getCode().equalsIgnoreCase("AC001")) {
				message="You do not have a saving account";
				return message;
			}
			
			//check if the customer account number balance is sufficient for transfer
			if(ownAccount.getAvBalance() <  transactionVO.getAmount()) {
				message="You do not have sufficient fund to transfer";
				return message;
			}
			
		}
		else {
			message="You do not have a valid saving account";
			return message;
		}
		
		////checking PAYEE account number is valid (TO account number)
		PayeeInfo payee= payeeInfoRepository.findById(transactionVO.getPayeeId()).get();
		
		//checking payeeAccount exists for not
		Optional<CustomerAccountInfo> payeeAccount=customerAccountInfoRepository.findAccountByAccountNumber(payee.getPayeeAccountNo());
		if(!payeeAccount.isPresent()) {
			message="Payee account number is not valid";
			return message;
		}
		
		//Check payee's account is not saving account
		
		CustomerAccountInfo pAccount=payeeAccount.get();
		if(!pAccount.getAccountType().getCode().equalsIgnoreCase("AC001")) {
			message="Payee account number is not valid";
			return message;
			
		}
		
	
		
		//update the remaining balance in customer account balance
		float availableAmount= ownAccount.getAvBalance() -  transactionVO.getAmount();
		ownAccount.setAvBalance(availableAmount);
		
		//saving into customerAccountInfoVO
		customerAccountInfoRepository.save(ownAccount);
		
		//update the remaining balance in payee account balance
		float balancePayeeAmount= pAccount.getAvBalance() +  transactionVO.getAmount();
		pAccount.setAvBalance(balancePayeeAmount);
		
		//update transaction table
		
		Transaction transaction=new Transaction();
		//BeanUtils.copyProperties(transactionVO, transaction,ignoreNullData(transactionVO));
		transaction.setAmount(transactionVO.getAmount());
		//PayeeInfo payeeInfo=new PayeeInfo();
		//payeeInfo.setId(transactionVO.getPayeeId());
		//transaction.setPayeeId(payeeInfo);
		transaction.setPayeeId(payee);
		transaction.setDebitAccountNumber(ownAccount.getAccountNumber());
		transaction.setDescription(transactionVO.getDescription());
		transaction.setTransactionDate(new Timestamp(new Date().getTime()));
		
		transactionRepository.save(transaction);
		return message;
	}
	
	
	@Override
	public List<TransactionVO> getTransactionList(String userId) {
		List<TransactionVO> transactionListVO=new ArrayList<>();
		Customer customer=customerRepository.findByEmail(userId).get();
		Optional<CustomerAccountInfo> account=customerAccountInfoRepository.findAccountByEmail(customer.getLogin());
		CustomerAccountInfo ownAccountNumber=account.get();
		List <Transaction> transactionList=transactionRepository.findAllTransactionforUser(ownAccountNumber.getAccountNumber());
		for(Transaction transaction:transactionList) {
			TransactionVO transactionVO=new TransactionVO();
			if(transaction.getDebitAccountNumber().equals(ownAccountNumber.getAccountNumber())) {
				transactionVO.setTransactionType("Debit");
			}
			else {
				transactionVO.setTransactionType("Credit");
			}
			transactionVO.setAmount(transaction.getAmount());
			transactionVO.setDescription(transaction.getDescription());
			transactionVO.setTransactionDate(transaction.getTransactionDate());
			PayeeInfoVO payee=new PayeeInfoVO();
			BeanUtils.copyProperties(transaction.getPayeeId(), payee);
			transactionVO.setPayee(payee);
			transactionListVO.add(transactionVO);
		}
		return transactionListVO;
		
		
		
	}
	
	
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

	
	
}
