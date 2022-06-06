package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.User;

public interface UserRepository extends JpaRepository<User,Long>{
	 Page<User> findByPublished(boolean published, Pageable pageable);
	 Page<User> findByListContaining(String list, Pageable pageable);
	 
	 @Query("SELECT t FROM User t WHERE t.name LIKE %?1%")
	 Page<User> findByNameLike(String name,Pageable pageable);
	 
	 @Query("SELECT t FROM User t WHERE t.address LIKE %?1%")
	 Page<User> findByAddressLike(String address,Pageable pageable);
	 
	 @Query("SELECT t FROM User t WHERE t.text LIKE %?1%")
	 Page<User> findByTextLike(String text,Pageable pageable);
	 
	 @Transactional
	 @Modifying
	 @Query("UPDATE User t SET t.published=false WHERE t.id=?1")
	 int publishUserFalse(Long id);
	 
	 @Transactional
	 @Modifying
	 @Query("UPDATE User t SET t.published=true WHERE t.id=?1")
	 int publishUser(Long id);
}
