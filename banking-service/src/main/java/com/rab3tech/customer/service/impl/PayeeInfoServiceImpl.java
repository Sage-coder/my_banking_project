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

import com.rab3tech.admin.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.CustomerSaving;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerSavingVO;
import com.rab3tech.vo.PayeeInfoVO;

@Transactional
@Service
public class PayeeInfoServiceImpl implements PayeeInfoService{
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private PayeeInfoRepository payeeInfoRepository;
	
	@Autowired
	private CustomerAccountInfoRepository customerAccountInfoRepository;
	
	
	
	@Override
	public List<PayeeInfoVO> findAllByCustomerId(String customerId) {
		List<PayeeInfo> payeeList = payeeInfoRepository.findByCustomerId(customerId);
		List<PayeeInfoVO> payeeListVOs= new ArrayList<>();
		for(PayeeInfo  PayeeInfos: payeeList) {
			PayeeInfoVO payeeInfoVOss=new PayeeInfoVO();
			BeanUtils.copyProperties(PayeeInfos, payeeInfoVOss);
			payeeListVOs.add(payeeInfoVOss);
		}
		return payeeListVOs;
	}
	
	@Override
	public String savePayee(PayeeInfoVO payeeInfoVO) {
		String message=null;
		//check user's account test
		Optional<CustomerAccountInfoVO> account =customerService.findByUserId(payeeInfoVO.getCustomerId());
		if(account.isPresent()) {
			CustomerAccountInfoVO ownAccount=account.get();
			/*if(!ownAccount.getAccount_status().equalsIgnoreCase("ACTIVE")) {
				message="You is not an ACTIVE account";
				return message;
			}*/
			if(!ownAccount.getAccountType().getCode().equalsIgnoreCase("AC001")) {
				message="You do not have a saving account";
				return message;
			}
			
		}
		else {
			message="You do not have a valid saving account";
			return message;
		}
		
			
		//nickname should be unique
		Optional<PayeeInfo> payee=payeeInfoRepository.findByPayeeNickNameAndCustomerId(payeeInfoVO.getPayeeNickName(), payeeInfoVO.getCustomerId());
		if(payee.isPresent()) {
			message="Beneficiary with same nick name already exists";
			return message;
		}
		
		//Check payee's account exist
		Optional<CustomerAccountInfo> payeeAccount=customerAccountInfoRepository.findAccountByAccountNumber(payeeInfoVO.getPayeeAccountNo());
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
		
		//check if the the given payee account number is equal to user's own account number
		if(pAccount.getCustomerId().getLoginid().equalsIgnoreCase(payeeInfoVO.getCustomerId())) {
			message="You cannot add your own account number as beneficiary";
			return message;
		}
	
		//check if same beneficiary already added
		if(payeeInfoRepository.findByPayeeAccountNoAndCustomerId(payeeInfoVO.getPayeeAccountNo(),payeeInfoVO.getCustomerId()).isPresent()) {
			message="Beneficiary with the same account already exists";
			return message;
		}
		
		PayeeInfo payeeDetails=new PayeeInfo();
		BeanUtils.copyProperties(payeeInfoVO, payeeDetails);
		payeeDetails.setDoe(new Timestamp(new Date().getTime()));
		payeeDetails.setDom(new Timestamp(new Date().getTime()));
		payeeDetails.setStatus("AS04");
		payeeInfoRepository.save(payeeDetails);
		message="Payee details has been added successfully";
		return message;
	
		}
	
	@Override
	public void deletePayee(int payeeId) {
		payeeInfoRepository.deleteById(payeeId);
	}
	
	@Override
	public PayeeInfoVO findPayeeId(int id) {
		PayeeInfoVO vo=new PayeeInfoVO();
		PayeeInfo payee= payeeInfoRepository.findById(id).get();
		BeanUtils.copyProperties(payee, vo);
		return vo;

	}
	
	@Override
	public void editPayee(PayeeInfoVO payeeInfoVO) throws Exception {
		
		PayeeInfo payee= payeeInfoRepository.findById(payeeInfoVO.getId()).get();
		BeanUtils.copyProperties(payeeInfoVO, payee,ignoreNullData(payeeInfoVO));
		payeeInfoRepository.save(payee);

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
	
	@Override
	public void persist(PayeeInfoVO payeeInfoVO) {
		PayeeInfo payee= payeeInfoRepository.findById(payeeInfoVO.getId()).get();
		BeanUtils.copyProperties(payeeInfoVO, payee,ignoreNullData(payeeInfoVO));
		payeeInfoRepository.save(payee);
		
	}
}
