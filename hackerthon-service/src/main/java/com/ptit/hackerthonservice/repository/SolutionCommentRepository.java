package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.SolutionComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SolutionCommentRepository extends JpaRepository<SolutionComment,Long> {
    @Query("SELECT sc FROM SolutionComment sc JOIN sc.solution sl  WHERE sl.title LIKE :value AND ( :solutionId IS NULL OR sl.id = :solutionId )")
    Page<SolutionComment> find(@Param("value") String value,@Param("solutionId") Long uid, Pageable pageable);

    Optional<SolutionComment> findByCommentId(Long commentId);
}
