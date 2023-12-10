package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.UserActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

	@Query("SELECT ua FROM UserActivity ua WHERE ua.createdBy.name LIKE :value or ua.description LIKE :value")
	Page<UserActivity> find(@Param("value") String value, Pageable pageable);
}
