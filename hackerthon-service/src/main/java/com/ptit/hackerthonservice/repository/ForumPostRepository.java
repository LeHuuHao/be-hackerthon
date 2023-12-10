package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.ForumPost;
import com.ptit.hackerthonservice.entity.Solution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {

    @Query("SELECT DISTINCT s FROM ForumPost s LEFT JOIN s.tags t   "
            + "WHERE s.title LIKE :title AND ( COALESCE(:tagIds) IS NULL OR t.id in (:tagIds) ) "
            + "AND (:uid IS NULL OR s.createdBy.id = :uid) ")
    Page<ForumPost> find(@Param("title") String value, @Param("uid") Integer uid,
                         @Param("tagIds") List<Integer> tagIds, Pageable pageable);


    Optional<ForumPost> findBySlug(String slug);

}