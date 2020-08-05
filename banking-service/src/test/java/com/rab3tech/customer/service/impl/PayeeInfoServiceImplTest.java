package com.rab3tech.customer.service.impl;

import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.rab3tech.admin.dao.repository.PayeeInfoRepository;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.vo.PayeeInfoVO;

@RunWith(MockitoJUnitRunner.class)
public class PayeeInfoServiceImplTest {

	@Mock
	private PayeeInfoRepository payeeInfoRepository;

	@InjectMocks
	private PayeeInfoServiceImpl payeeInfoService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	/*
	 * @Override public List<PayeeInfoVO> findAllByCustomerId(String customerId) {
	 * List<PayeeInfo> payeeList = payeeInfoRepository.findByCustomerId(customerId);
	 * List<PayeeInfoVO> payeeListVOs= new ArrayList<>(); for(PayeeInfo PayeeInfos:
	 * payeeList) { PayeeInfoVO payeeInfoVOss=new PayeeInfoVO();
	 * BeanUtils.copyProperties(PayeeInfos, payeeInfoVOss);
	 * payeeListVOs.add(payeeInfoVOss); } return payeeListVOs; }
	 * 
	 * PayeeInfo(int id, String payeeAccountNo, String payeeName, String
	 * payeeNickName, String customerId, Timestamp doe, Timestamp dom, String
	 * remarks, String status, int urn, String email)
	 */

	@Test
	public void testfindAllByCustomerIdWhenExist() {

		List<PayeeInfo> payees = new ArrayList<>();
		PayeeInfo p1 = new PayeeInfo(1, "123", "Mary Davis", "Mary", "cs123", null, null, "Good Standing", "Active", 1,
				"mary@gmail.com");
		PayeeInfo p2 = new PayeeInfo(2, "234", "Harry Davis", "Harry", "cs123", null, null, "Good Standing", "Active",
				2, "harry@gmail.com");
		payees.add(p1);
		payees.add(p2);
		when(payeeInfoRepository.findByCustomerId("cs123")).thenReturn(payees);
		List<PayeeInfoVO> result = payeeInfoService.findAllByCustomerId("cs123");

		assertNotNull(result);
		assertEquals(result.size(), 2);
		assertEquals(result.get(0).getPayeeName(), "Mary Davis");
		assertEquals(result.get(1).getPayeeName(), "Harry Davis");
		verify(payeeInfoRepository, times(1)).findByCustomerId("cs123");
		verifyNoMoreInteractions(payeeInfoRepository);
	}
}
