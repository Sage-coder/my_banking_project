package com.rab3tech.customer.service;

import java.util.List;

import com.rab3tech.vo.CustomerRequestVO;
import com.rab3tech.vo.RequestTypeVO;

public interface CustomerRequestService {

	RequestTypeVO findById(int id);

	List<RequestTypeVO> findAllRequests();

	String submitRequestByEmail(String userId);

	boolean emailNotExist(String email);

}
