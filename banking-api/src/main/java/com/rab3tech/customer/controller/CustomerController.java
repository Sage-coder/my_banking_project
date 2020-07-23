package com.rab3tech.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rab3tech.customer.service.impl.PayeeInfoService;
import com.rab3tech.vo.ApplicationResponseVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.PayeeInfoVO;

@RestController
@CrossOrigin(origins = "*")

public class CustomerController {
	@Autowired
	private PayeeInfoService payeeInfoService;
	
	@GetMapping("/customer/deletePayee/{payeeId}")
	public ApplicationResponseVO deletePayee(@PathVariable int payeeId) {
		ApplicationResponseVO vo=new ApplicationResponseVO();
		payeeInfoService.deletePayee(payeeId);
		vo.setCode(200);
		vo.setStatus("success");
		vo.setMessage("Payee has been deleted");
		return vo;
	}
	
	@GetMapping("/customer/payeeInfo/{payeeId}")
	public PayeeInfoVO payeeInfo(@PathVariable int payeeId) {
		PayeeInfoVO vo=new PayeeInfoVO();
		vo=payeeInfoService.findPayeeId(payeeId);
		return vo;
		
	}
	
	/*
	@PostMapping("/customer/editPayee")
	public ApplicationResponseVO editInfo(@RequestBody PayeeInfoVO payeeInfoVO) {
		ApplicationResponseVO vo=new ApplicationResponseVO();
		try {
			payeeInfoService.editPayee(payeeInfoVO);
			vo.setCode(200);
			vo.setStatus("success");
			vo.setMessage("Payee has been updated successfully");
	
		}
		catch(Exception e) {
			vo.setCode(404);
			vo.setStatus("error");
			vo.setMessage("Some error must have occured at the time of editing ");
		}
		return vo;
	}*/
	
	@PostMapping("/customer/editPayee")
	public ApplicationResponseVO editInfo(@RequestBody PayeeInfoVO payeeInfoVO) {
		payeeInfoService.persist(payeeInfoVO);
		ApplicationResponseVO vo=new ApplicationResponseVO();
		vo.setCode(200);
		vo.setStatus("success");
		vo.setMessage("Payee has been updated successfully");

		/*{
			"code":"O8182",
			"message":profile is created""
		}*/
		return vo;
	}


}
