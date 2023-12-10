package com.ptit.hackerthonservice.repository;


import com.ptit.hackerthonservice.entity.Tag;
import com.ptit.hackerthonservice.utils.TagTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepo extends JpaRepository<Tag, Integer> {
	@Query("select t FROM Tag t WHERE t.title LIKE :title and ( :type is null OR t.type = :type )")
	Page<Tag> find(@Param("title") String value, @Param("type") TagTypeEnum type, Pageable pageable);

	Optional<Tag> findBySlug(String slug);
}
