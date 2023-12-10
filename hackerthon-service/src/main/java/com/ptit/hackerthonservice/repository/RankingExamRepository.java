package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.RankingExam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RankingExamRepository extends JpaRepository<RankingExam,Long> {

    @Query("SELECT re FROM RankingExam re JOIN re.exam e WHERE e.title LIKE :title AND (:examId IS NULL OR e.id=:examId) ")
    Page<RankingExam> find(@Param("title") String value, @Param("examId") Long examId, Pageable pageable);


    @Query("SELECT re FROM RankingExam re JOIN re.createdBy u WHERE u.id=:uid")
    Optional<RankingExam> checkUser(@Param("uid") Integer uid);
}
