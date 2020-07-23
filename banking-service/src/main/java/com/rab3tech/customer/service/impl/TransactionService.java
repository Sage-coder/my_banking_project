package com.rab3tech.customer.service.impl;

import java.util.List;

import com.rab3tech.customer.dao.repository.Transaction;
import com.rab3tech.vo.TransactionVO;

public interface TransactionService {
	
	String transferFund(TransactionVO transactionVO);



	List<TransactionVO> getTransactionList(String userId);
}
