package com.rab3tech.customer.service.impl;

import java.util.List;
import java.util.Optional;

import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.vo.CustomerSavingVO;
import com.rab3tech.vo.PayeeInfoVO;

public interface PayeeInfoService {

	String savePayee(PayeeInfoVO payeeInfoVO);

	List<PayeeInfoVO> findAllByCustomerId(String customerId);

	void deletePayee(int payeeId);


	PayeeInfoVO findPayeeId(int id);

	void editPayee(PayeeInfoVO payeeInfoVO) throws Exception;

	void persist(PayeeInfoVO payeeInfoVO);
	
	

}
