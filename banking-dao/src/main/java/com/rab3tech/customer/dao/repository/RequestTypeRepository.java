package com.rab3tech.customer.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rab3tech.dao.entity.RequestType;

public interface RequestTypeRepository extends JpaRepository<RequestType, Integer> {
	public RequestType findByName(String name);
	public RequestType findByStatus(int status);
	public RequestType findByNameAndStatus(String name,int status);
}
