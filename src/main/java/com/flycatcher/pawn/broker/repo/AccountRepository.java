package com.flycatcher.pawn.broker.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flycatcher.pawn.broker.model.Account;

/**
 * <p> Account Repository </p>
 * @author kumar
 * @version 1.0.0
 * @since 19-Mar-2017
 * 
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
	
	@Query("SELECT a FROM Account a WHERE " +
            "(LOWER(a.accountNumber) LIKE LOWER(CONCAT('%',?1, '%')) OR "
            +" LOWER(a.firstName) LIKE LOWER(CONCAT('%',?1, '%')) OR "
            +" LOWER(a.lastName) LIKE LOWER(CONCAT('%',?1, '%')) OR "
            +" LOWER(a.pinCode) LIKE LOWER(CONCAT('%',?1, '%')) OR "
            +" LOWER(a.state) LIKE LOWER(CONCAT('%',?1, '%')) OR "
            +" LOWER(a.area) LIKE LOWER(CONCAT('%',?1, '%')) OR "
            +" LOWER(a.city) LIKE LOWER(CONCAT('%',?1, '%')) OR "
            +" LOWER(a.fatherName) LIKE LOWER(CONCAT('%',?1, '%')))" )
	public Page<Account> findAccountByPage(String search,Pageable pageable);

}


