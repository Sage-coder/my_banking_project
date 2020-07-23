package com.rab3tech.customer.ui.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.customer.service.LoginService;
import com.rab3tech.customer.service.impl.CustomerAddressService;
import com.rab3tech.customer.service.impl.CustomerEnquiryService;
import com.rab3tech.customer.service.impl.PayeeInfoService;
import com.rab3tech.customer.service.impl.SecurityQuestionService;
import com.rab3tech.customer.service.impl.TransactionService;
import com.rab3tech.email.service.EmailService;
import com.rab3tech.vo.ChangePasswordVO;
import com.rab3tech.vo.CustomerSavingVO;
import com.rab3tech.vo.CustomerSecurityQueAnsVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.EmailVO;
import com.rab3tech.vo.LoginVO;
import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.SecurityQuestionsVO;
import com.rab3tech.vo.TransactionVO;

/**
 * 
 * @author nagendra
 * This class for customer GUI
 *
 */
@Controller
public class CustomerUIController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerUIController.class);

	@Autowired
	private CustomerEnquiryService customerEnquiryService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	   private TransactionService transactionService;
	
	@Autowired
	private SecurityQuestionService securityQuestionService;
	
	@Autowired
	private PayeeInfoService payeeInfoService;
	
	@Autowired
	private CustomerAddressService customerAddressService;
	
	@GetMapping("/customer/account/showRaiseRequest")
	public String showChequeBookRequestPage( Model model,HttpSession session) {
		String message="sweta bajracharya";
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		
		if(loginVO2!=null) {
		String userId=loginVO2.getUsername();
		boolean result=customerAddressService.checkAddressPresentOrNot(userId);
			if(result) {
			return "customer/AddressPage";
			}
			else {
				return "customer/dashboard";
			}
		
		}
		else {
			model.addAttribute("error","PLease login to proceed further");
			return "customer/login";}
		}
		
	
	
	//Account Statement
	@GetMapping("/customer/account/showAccountStatement")
	public String showAccountStatementPage( Model model,HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2!=null) {
		String userId=loginVO2.getUsername();
		List<TransactionVO> transactionList = transactionService.getTransactionList(userId);
		model.addAttribute("transactionList", transactionList);
		return "customer/accountStatement";
		}
		else {
			model.addAttribute("error","PLease login to proceed further");
			return "customer/login";
		}
	}
	
	
	@GetMapping("/customer/acount/showfundTransfer")
	public String showFundTransferPage( Model model,HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2!=null) {
		List<PayeeInfoVO> payeeList = payeeInfoService.findAllByCustomerId(loginVO2.getUsername());
		model.addAttribute("payeeList", payeeList);
		return "customer/fundTranfer";
		}
		else {
			model.addAttribute("error","PLease login to proceed further");
			return "customer/login";
		}
	}
	
	@PostMapping("/customer/account/submitfundTransfer")
	public String submitFundTransferPage( @ModelAttribute TransactionVO transactionVO,HttpSession session,Model model) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		if(loginVO2!=null) {
			transactionVO.setCustomerId(loginVO2.getUsername());
			String message=transactionService.transferFund(transactionVO);
			model.addAttribute("message",message);
			return "customer/fundTranfer";
		}
		else {
			model.addAttribute("error","PLease login to proceed further");
			return "customer/login";
		}
		
	}
	
	@GetMapping("/customer/showPayeeList")
	public String showPayeeListPage(HttpSession session, Model model) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		
		if(loginVO2!=null) {
			
			List<PayeeInfoVO> payeeList = payeeInfoService.findAllByCustomerId(loginVO2.getUsername());
			model.addAttribute("payeeList", payeeList);
			return "customer/payeeList";
		}
		else {
			model.addAttribute("error","PLease login to proceed further");
			return "customer/login";
		}
		
	}
	
	@GetMapping("/customer/addPayee")
	public String showAddPayeePage(HttpSession session, Model model) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		
		if(loginVO2!=null) {
			return "/customer/addPayee";
		}
		else {
			model.addAttribute("error","PLease login to proceed further");
			return "customer/login";
		}
		
	}
	
	///for add payee
	
	@PostMapping("/customer/account/savePayee")
	public String createAddPayeePage(@ModelAttribute PayeeInfoVO payeeInfoVO,HttpSession session, Model model) {
		logger.debug("savePayee"+payeeInfoVO);
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		
		if(loginVO2!=null) {
			payeeInfoVO.setCustomerId(loginVO2.getUsername());
		
			String message=payeeInfoService.savePayee(payeeInfoVO);
			model.addAttribute("message",message);
		}
	else {
		model.addAttribute("error","PLease login to proceed further");
		return "customer/login";
		}
		return "/customer/addPayee";
		}
	
	
	@PostMapping("/customer/account/editPayeeInfo")
	public String editPayeePage(@ModelAttribute PayeeInfoVO payeeInfoVO,HttpSession session, Model model) {
		logger.debug("savePayee"+payeeInfoVO);
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		
		if(loginVO2!=null) {
			payeeInfoVO.setCustomerId(loginVO2.getUsername());
			try {
				payeeInfoService.editPayee(payeeInfoVO);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			model.addAttribute("message","payee details has been updated");
		}
	else {
		model.addAttribute("error","PLease login to proceed further");
		return "customer/login";
		}
		return "redirect:/customer/showPayeeList";
		}
	
	@PostMapping("/customer/showQuestion")
	public String showQuestions( @RequestParam String username,Model model) {
		Optional<LoginVO> optional=loginService.findUserByUsername(username);
		if(optional.isPresent()) {
			CustomerSecurityQueAnsVO question=securityQuestionService.getSecurityDetailsForUser(username);
			model.addAttribute("question", question);//to show in the html page
		}
		else {
		
		model.addAttribute("error", "sorry , the username is not valid");
		return "redirect:/customer/showQuestion";}
		
		return "/customer/showQuestionAnswerView";
	
	}

	@PostMapping("/customer/validateSecurityQuestion")
	public String validateQuestion( @ModelAttribute CustomerSecurityQueAnsVO customerSecurityQueAnsVO,Model model) {
		Optional<LoginVO> optional=loginService.findUserByUsername(customerSecurityQueAnsVO.getLoginid());
		if(optional.isPresent()) {
			boolean result=securityQuestionService.validateSecurityQuestions(customerSecurityQueAnsVO);
			if(result) {
				model.addAttribute("loginid", optional.get().getUsername());
				return "customer/forgetChangePassword";
				}
			else{
				model.addAttribute("message", "You cannot change your password");
				model.addAttribute("username", optional.get().getUsername());
				return "customer/showQuestionAnswer";
			}
		}
			
			else {
				
				model.addAttribute("error", "sorry , the username is not valid");
				return "redirect:/customer/showQuestion";
				}
				
				
	}
	@PostMapping("/customer/forgetChangePassword")
	public String validateQuestion(@ModelAttribute ChangePasswordVO changePasswordVO, Model model) {
		Optional<LoginVO> optional=loginService.findUserByUsername(changePasswordVO.getLoginid());
		if(optional.isPresent()) {
			if(changePasswordVO.getNewPassword().equals(changePasswordVO.getConfirmPassword())) {
				loginService.changePassword(changePasswordVO);
			}else {
				model.addAttribute("error", "sorry , the new password and confirm passwprd are not same");
				return "customer/forgetChangePassword";
			}
		}else {
				model.addAttribute("error", "sorry , the username is not valid");
				return "customer/login";
				}
		model.addAttribute("error", changePasswordVO.getLoginid()+"your new password has been set successfully");
		
		
		return "customer/login";
		
	}	
	

	@PostMapping("/customer/changePassword")
	public String saveCustomerQuestions(@ModelAttribute ChangePasswordVO changePasswordVO, Model model,HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		String loginid=loginVO2.getUsername();
		changePasswordVO.setLoginid(loginid);
		String viewName ="customer/dashboard";
		boolean status=loginService.checkPasswordValid(loginid,changePasswordVO.getCurrentPassword());
		if(status) {
			if(changePasswordVO.getNewPassword().equals(changePasswordVO.getConfirmPassword())) {
				 viewName ="customer/dashboard";
				 loginService.changePassword(changePasswordVO);
			}else {
				model.addAttribute("error","Sorry , your new password and confirm passwords are not same!");
				return "customer/login";	//login.html	
			}
		}else {
			model.addAttribute("error","Sorry , your username and password are not valid!");
			return "customer/login";	//login.html	
		}
		return viewName;
	}
	
	@PostMapping("/customer/securityQuestion")
	public String saveCustomerQuestions(@ModelAttribute("customerSecurityQueAnsVO") CustomerSecurityQueAnsVO customerSecurityQueAnsVO, Model model,HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		String loginid=loginVO2.getUsername();
		customerSecurityQueAnsVO.setLoginid(loginid);
		securityQuestionService.save(customerSecurityQueAnsVO);
		
		return "customer/chagePassword";
	}

		// http://localhost:444/customer/account/registration?cuid=1585a34b5277-dab2-475a-b7b4-042e032e8121603186515
	@GetMapping("/customer/account/registration")
	public String showCustomerRegistrationPage(@RequestParam String cuid, Model model) {

		logger.debug("cuid = " + cuid);
		Optional<CustomerSavingVO> optional = customerEnquiryService.findCustomerEnquiryByUuid(cuid);
		CustomerVO customerVO = new CustomerVO();

		if (!optional.isPresent()) {
			return "customer/error";
		} else {
			// model is used to carry data from controller to the view =- JSP/
			CustomerSavingVO customerSavingVO = optional.get();
			customerVO.setEmail(customerSavingVO.getEmail());
			customerVO.setName(customerSavingVO.getName());
			customerVO.setMobile(customerSavingVO.getMobile());
			customerVO.setAddress(customerSavingVO.getLocation());
			customerVO.setToken(cuid);
			logger.debug(customerSavingVO.toString());
			// model - is hash map which is used to carry data from controller to thyme
			// leaf!!!!!
			// model is similar to request scope in jsp and servlet
			model.addAttribute("customerVO", customerVO);
			return "customer/customerRegistration"; // thyme leaf
		}
	}

	@PostMapping("/customer/account/registration")
	public String createCustomer(@ModelAttribute CustomerVO customerVO, Model model) {
		// saving customer into database
		logger.debug(customerVO.toString());
		customerVO = customerService.createAccount(customerVO);
		// Write code to send email

		EmailVO mail = new EmailVO(customerVO.getEmail(), "javahunk2020@gmail.com",
				"Regarding Customer " + customerVO.getName() + "  userid and password", "", customerVO.getName());
		mail.setUsername(customerVO.getUserid());
		mail.setPassword(customerVO.getPassword());
		emailService.sendUsernamePasswordEmail(mail);
		System.out.println(customerVO);
		model.addAttribute("loginVO", new LoginVO());
		model.addAttribute("message", "Your account has been setup successfully , please check your email.");
		return "customer/login";
	}

	@GetMapping(value = { "/customer/account/enquiry", "/", "/mocha", "/welcome" })
	public String showCustomerEnquiryPage(Model model) {
		CustomerSavingVO customerSavingVO = new CustomerSavingVO();
		// model is map which is used to carry object from controller to view
		model.addAttribute("customerSavingVO", customerSavingVO);
		return "customer/customerEnquiry"; // customerEnquiry.html
	}

	@PostMapping("/customer/account/enquiry")
	public String submitEnquiryData(@ModelAttribute CustomerSavingVO customerSavingVO, Model model) {
		boolean status = customerEnquiryService.emailNotExist(customerSavingVO.getEmail());
		logger.info("Executing submitEnquiryData");
		if (status) {
			CustomerSavingVO response = customerEnquiryService.save(customerSavingVO);
			logger.debug("Hey Customer , your enquiry form has been submitted successfully!!! and appref "
					+ response.getAppref());
			model.addAttribute("message",
					"Hey Customer , your enquiry form has been submitted successfully!!! and appref "
							+ response.getAppref());
		} else {
			model.addAttribute("message", "Sorry , this email is already in use " + customerSavingVO.getEmail());
		}
		return "customer/success"; // customerEnquiry.html

	}
	
	
	
	@GetMapping("/customer/updateProfile")
	public String showUpdatePage(@ModelAttribute CustomerVO customerVOs,HttpSession session, Model model) {

		LoginVO loginVO2= (LoginVO)session.getAttribute("userSessionVO");
		String loginid=loginVO2.getUsername();
		customerVOs.setEmail(loginid);
		if(loginVO2 !=null) {
			CustomerVO customerVO=customerService.findByEmail(loginVO2.getUsername());
			model.addAttribute("customerVO", customerVO);
			List<SecurityQuestionsVO> questionsVOs=securityQuestionService.findAll();
			model.addAttribute("questionsVOs", questionsVOs);
		}
		else {
			model.addAttribute("error", "Please login to proceed further");
			
		}
		return "customer/updateProfile";
	}
	

	
	@PostMapping("/customer/account/myAccountUpdate")
	public String createUpdateCustomer(@ModelAttribute CustomerVO customerVO, Model model,HttpSession session) {
		// saving customer into database
		logger.debug(customerVO.toString());
		LoginVO loginVO2= (LoginVO)session.getAttribute("userSessionVO");
		
		if(loginVO2 !=null) {
			customerService.updateCustomer(customerVO);
			model.addAttribute("message", "Your account has been setup");
			
		
		}
		else {
			model.addAttribute("error", "Please login to try the proceed further");
			return "customer/login";
		}
		logger.debug(customerVO.toString());
		return "customer/updateProfile";
	}
	
}
