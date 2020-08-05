package com.rab3tech.vo;

import java.util.Date;

import com.rab3tech.dao.entity.AccountStatus;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAddress;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.RequestType;

public class CustomerRequestVO {
	private int id;
	private String reqRefNumber;
	private Customer customerId;
	private CustomerAddress addressId;
	private RequestType requestType;
	private RequestType status;
	private  Date doe;
	private Date doa;
	private String email;
	
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReqRefNumber() {
		return reqRefNumber;
	}
	public void setReqRefNumber(String reqRefNumber) {
		this.reqRefNumber = reqRefNumber;
	}
	public  Customer getCustomerId() {
		return customerId;
	}
	public void setCustomerId( Customer customerId) {
		this.customerId = customerId;
	}
	public CustomerAddress getAddressId() {
		return addressId;
	}
	public void setAddressId(CustomerAddress addressId) {
		this.addressId = addressId;
	}
	public RequestType getRequestType() {
		return requestType;
	}
	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}
	public RequestType getStatus() {
		return status;
	}
	public void setStatus(RequestType status) {
		this.status = status;
	}
	public Date getDoe() {
		return doe;
	}
	public void setDoe(Date doe) {
		this.doe = doe;
	}
	public Date getDoa() {
		return doa;
	}
	public void setDoa(Date doa) {
		this.doa = doa;
	}
	
	
}
