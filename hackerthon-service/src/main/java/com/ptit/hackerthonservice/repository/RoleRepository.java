package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByName(String name);

	@Query("SELECT r FROM Role r WHERE r.name LIKE :role ")
	Page<Role> find(@Param("role") String value, Pageable pageable);
}
