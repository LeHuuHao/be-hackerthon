package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.dto.StatisticUserDTO;
import com.ptit.hackerthonservice.entity.Exam;
import com.ptit.hackerthonservice.entity.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ExamRepo extends JpaRepository<Exam, Long> {
	@Query("SELECT ex FROM Exam ex WHERE ex.title LIKE :title ")
	Page<Exam> find(@Param("title") String value, Pageable pageable);


	Optional<Exam> findBySlug(String slug);
}
