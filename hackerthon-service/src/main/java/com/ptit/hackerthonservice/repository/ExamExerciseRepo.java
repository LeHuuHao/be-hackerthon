package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.ExamExercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ExamExerciseRepo extends JpaRepository<ExamExercise,Integer> {
    @Query("SELECT ex FROM ExamExercise ex JOIN ex.exam e WHERE e.title LIKE :title AND (:examId IS NULL OR e.id=:examId) ")
    Page<ExamExercise> find(@Param("title") String value,@Param("examId") Long examId, Pageable pageable);

}
