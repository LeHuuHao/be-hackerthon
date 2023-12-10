package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.Answer;
import com.ptit.hackerthonservice.entity.RankingExam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepo extends JpaRepository<Answer, Long> {

	@Query("SELECT a FROM Answer a JOIN a.createdBy u JOIN a.exercise ex WHERE ex.title LIKE :value AND (:eid is NULL OR ex.id=:eid) AND u.id =:uid ")
	Page<Answer> find(@Param("value") String value,@Param("eid") Long exerciseId, @Param("uid") Integer uid, Pageable pageable);

	@Query("SELECT COUNT(a.id)\n" + "FROM Answer a JOIN a.createdBy u\n"
			+ "JOIN a.exercise ex  WHERE WEEK(a.createdAt) = WEEK(NOW()) - 1\n"
			+ "  AND YEAR(a.createdAt) = YEAR(NOW()) and u.id= :uid\n")
	Long findAnswerByWeek(@Param("uid") Integer uid);

	@Query("SELECT MAX(a) FROM Answer a WHERE a.createdBy.id=:uid AND a.statusCode=100 AND a.exercise.id=:exerciseId")
	Optional<Answer> checkUser(@Param("uid") Integer uid,@Param("exerciseId") Long exerciseId);

}
