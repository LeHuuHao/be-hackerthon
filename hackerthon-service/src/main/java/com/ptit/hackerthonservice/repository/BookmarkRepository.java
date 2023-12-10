package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	@Query("SELECT b FROM Bookmark b WHERE b.exercise.title LIKE :value AND b.createdBy.id= :uid")
	Page<Bookmark> find(@Param("value") String value, @Param("uid") Integer uid, Pageable pageable);

	@Query("SELECT b FROM Bookmark b WHERE b.createdBy.id= :uid AND b.exercise.id= :exId")
	Bookmark getBookMarkByUserId(@Param("uid") Integer uid, @Param("exId") Long exId);
}
