package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.Privilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
	@Query("SELECT p FROM Privilege p WHERE p.api LIKE :api ")
	Page<Privilege> find(@Param("api") String value, Pageable pageable);
	
    Optional<Privilege> findByAuthority(String authority);

}
