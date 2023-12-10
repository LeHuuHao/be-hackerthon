package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.Solution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {

	@Query("SELECT DISTINCT s FROM Solution s LEFT JOIN s.tags t LEFT JOIN s.exerciseSolution se  "
			+ "WHERE s.title LIKE :title AND ( COALESCE(:tagIds) IS NULL OR t.id in (:tagIds) ) "
			+ "AND ( :exerciseId IS NULL OR se.exercise.id = :exerciseId ) AND (:uid IS NULL OR s.createdBy.id = :uid) ")
	Page<Solution> find(@Param("title") String value, @Param("exerciseId") Long exerciseId, @Param("uid") Integer uid,
						@Param("tagIds") List<Integer> tagIds, Pageable pageable);

	@Query("SELECT DISTINCT s FROM Solution s LEFT JOIN s.tags t LEFT JOIN s.exerciseSolution se  "
			+ "WHERE s.title LIKE :title AND ( COALESCE(:tagIds) IS NULL OR t.id in (:tagIds) ) "
			+ "AND ( :exerciseId IS NULL OR se.exercise.id = :exerciseId ) AND (:uid IS NULL OR s.createdBy.id = :uid) ")
	Page<Solution> findExerciseSolutions(@Param("title") String value, @Param("exerciseId") Long exerciseId, @Param("uid") Integer uid,
										 @Param("tagIds") List<Integer> tagIds, Pageable pageable);

	@Query("SELECT COUNT(s.id)\n" + "FROM Solution s JOIN s.createdBy u\n"
			+ "WHERE WEEK(s.createdAt) = WEEK(NOW()) - 1\n" + "  AND YEAR(s.createdAt) = YEAR(NOW())and u.id=:uid")
	Long findSolutionByWeek(@Param("uid") Integer uid);


	Optional<Solution> findBySlug(String slug);

}
