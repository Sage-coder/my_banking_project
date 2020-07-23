package com.rab3tech.customer.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	@Query(value="SELECT u FROM Transaction u where u.debitAccountNumber = :accountNumber or u.payeeId.payeeAccountNo= :accountNumber order by u.transactionDate desc")
	List<Transaction> findAllTransactionforUser(@Param("accountNumber") String accountNumber);
}
//@Query("SELECT f FROM FundTransfer f where f.debtAccountNumber=:accountNumber or f.payeeid.payeeAccountNo =:accountNumber order by f.date desc")
//List<FundTransfer> getAllTransactions(@Param("accountNumber") String accountNumber);

