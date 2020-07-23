package com.rab3tech.customer.service.impl;


import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rab3tech.admin.dao.repository.CustomerSecurityQuestionsRepository;
import com.rab3tech.admin.dao.repository.MagicCustomerRepository;
import com.rab3tech.customer.dao.repository.CustomerQuestionsAnsRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.customer.dao.repository.SecurityQuestionsRepository;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerQuestionAnswer;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.SecurityQuestions;
import com.rab3tech.vo.CheckSecurityQuestionVO;
import com.rab3tech.vo.CustomerSecurityQueAnsVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.SecurityQuestionsVO;

@Transactional
@Service
public class SecurityQuestionServiceImpl implements SecurityQuestionService {

	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private SecurityQuestionsRepository questionsRepository;
	
	@Autowired
	private MagicCustomerRepository customerRepository;
	
	
	@Autowired
	private CustomerQuestionsAnsRepository customerQuestionsAnsRepository;
	@Autowired
	private CustomerSecurityQuestionsRepository customerSecurityQuestionsRepository;
	
	@Override
	public void save(CustomerSecurityQueAnsVO customerSecurityQueAnsVO) {
		//Deleting all customer questions for existing user
				List<CustomerQuestionAnswer> customerQuestionAnswers=customerQuestionsAnsRepository.findQuestionAnswer(customerSecurityQueAnsVO.getLoginid());
				if(customerQuestionAnswers.size()>0)
				customerQuestionsAnsRepository.deleteAll(customerQuestionAnswers);

				CustomerQuestionAnswer customerQuestionAnswer1=new CustomerQuestionAnswer();
				Login login=loginRepository.findById(customerSecurityQueAnsVO.getLoginid()).get();

				String quetionText=questionsRepository.findById(Integer.parseInt(customerSecurityQueAnsVO.getSecurityQuestion1())).get().getQuestions();
				//String quetionText=customerSecurityQueAnsVO.getSecurityQuestion1();
				customerQuestionAnswer1.setQuestion(quetionText);
				customerQuestionAnswer1.setAnswer(customerSecurityQueAnsVO.getSecurityQuestionAnswer1());
				customerQuestionAnswer1.setDoe(new Timestamp(new Date().getTime()));
				customerQuestionAnswer1.setDom(new Timestamp(new Date().getTime()));
				customerQuestionAnswer1.setLogin(login);
		
				customerQuestionsAnsRepository.save(customerQuestionAnswer1);

				CustomerQuestionAnswer customerQuestionAnswer2=new CustomerQuestionAnswer();
				quetionText=questionsRepository.findById(Integer.parseInt(customerSecurityQueAnsVO.getSecurityQuestion2())).get().getQuestions();
				customerQuestionAnswer2.setQuestion(quetionText);
				customerQuestionAnswer2.setAnswer(customerSecurityQueAnsVO.getSecurityQuestionAnswer2());
				customerQuestionAnswer2.setDoe(new Timestamp(new Date().getTime()));
				customerQuestionAnswer2.setDom(new Timestamp(new Date().getTime()));
				customerQuestionAnswer2.setLogin(login);
				customerQuestionsAnsRepository.save(customerQuestionAnswer2);

			}

	
	
	@Override
	public List<SecurityQuestionsVO>  findAll(){
		List<SecurityQuestions>  securityQuestions=questionsRepository.findAll();
		List<SecurityQuestionsVO> questionsVOs=new ArrayList<>();
		for(SecurityQuestions questions:securityQuestions) {
			SecurityQuestionsVO questionsVO=new SecurityQuestionsVO();
			BeanUtils.copyProperties(questions, questionsVO);
			questionsVOs.add(questionsVO);
		}
		return questionsVOs;
		/*return securityQuestions.stream().map(tt->{
			SecurityQuestionsVO questionsVO=new SecurityQuestionsVO();
			BeanUtils.copyProperties(tt, questionsVO);
			return questionsVO;
		}).collect(Collectors.toList());*/
	}

	  



	@Override
	public CustomerSecurityQueAnsVO getSecurityDetailsForUser(String username) {
		 
		CustomerSecurityQueAnsVO q=new CustomerSecurityQueAnsVO();
		List<CustomerQuestionAnswer> qAnswer=customerQuestionsAnsRepository.findQuestionAnswer(username);
		//String questionText1=customerSecurityQuestionsRepository.findById(Integer.parseInt(qAnswer.get(0).getQuestion())).get().getQuestions();
		//String questionText2=customerSecurityQuestionsRepository.findById(Integer.parseInt(qAnswer.get(1).getQuestion())).get().getQuestions();
		
		
		//q.setSecurityQuestion1(questionText1);
		//q.setSecurityQuestion2(questionText2);

		q.setSecurityQuestion1(qAnswer.get(0).getQuestion());
		q.setSecurityQuestion2(qAnswer.get(1).getQuestion());
		q.setLoginid(username);
		return q;
	}
	
	@Override
	public boolean validateSecurityQuestions(CustomerSecurityQueAnsVO vo) {
		boolean validate=false;
		List<CustomerQuestionAnswer> qAnswer=customerQuestionsAnsRepository.findQuestionAnswer(vo.getLoginid());
		if(qAnswer.get(0).getQuestion().equalsIgnoreCase(vo.getSecurityQuestion1()) && qAnswer.get(0).getAnswer().equalsIgnoreCase(vo.getSecurityQuestionAnswer1())) {
			validate=true;}
		if(qAnswer.get(1).getQuestion().equalsIgnoreCase(vo.getSecurityQuestion2()) && qAnswer.get(1).getAnswer().equalsIgnoreCase(vo.getSecurityQuestionAnswer2())) {
			validate=true;}
		else {
			validate=false;
		}
		
		return validate;
		
		
	}



	

	/*
	@Override
	public List<CustomerSecurityQueAnsVO> getSecurityDetailsForUser(String username) {
		List<CustomerQuestionAnswer> qAnswer=customerQuestionsAnsRepository.findQuestionAnswer(username);
		List<CustomerSecurityQueAnsVO> q1=new ArrayList<>();
		for(CustomerQuestionAnswer questions:qAnswer) {
			CustomerSecurityQueAnsVO questionsVO=new CustomerSecurityQueAnsVO();
			questionsVO.setSecurityQuestion1(qAnswer.get(0).getQuestion());
			questionsVO.setSecurityQuestion2(qAnswer.get(1).getQuestion());
			q1.add( questionsVO);
		}
		
		return q1;}*/
}