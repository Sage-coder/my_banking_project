package com.rab3tech.dao.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="customer_request_tbl")
public class CustomerRequest {
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
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="customerId",nullable=false)
	public Customer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Customer customerId) {
		this.customerId = customerId;
	}
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="addressId",nullable=false)
	public CustomerAddress getAddressId() {
		return addressId;
	}
	public void setAddressId(CustomerAddress addressId) {
		this.addressId = addressId;
	}
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="requestType",nullable=false)
	public RequestType getRequestType() {
		return requestType;
	}
	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="status",nullable=false)
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
