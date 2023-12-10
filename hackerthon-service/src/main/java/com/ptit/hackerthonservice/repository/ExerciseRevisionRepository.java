package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.ExerciseRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExerciseRevisionRepository extends JpaRepository<ExerciseRevision, Integer> {

	@Modifying
	@Query("delete from ExerciseRevision pr where pr.exercise.id = :eid")
	void deleteByExerciseId(@Param("eid") long eid);
}
