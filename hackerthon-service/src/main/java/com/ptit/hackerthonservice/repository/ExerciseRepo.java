package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.Exercise;
import com.ptit.hackerthonservice.utils.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepo extends JpaRepository<Exercise, Long> {

	@Query("SELECT DISTINCT ex FROM Exercise ex LEFT JOIN ex.tags t  WHERE ex.title LIKE :title AND ( COALESCE(:tagIds) IS NULL OR t.id in (:tagIds) ) "
			+ "AND ( :status IS NULL OR ex.status = :status ) AND ( :uid IS NULL OR ex.createdBy.id = :uid )")
	Page<Exercise> find(@Param("title") String value, @Param("tagIds") List<Integer> tagIds,
						@Param("status") StatusEnum status, @Param("uid") String uid, Pageable pageable);

	Optional<Exercise> findBySlug(String slug);
}
