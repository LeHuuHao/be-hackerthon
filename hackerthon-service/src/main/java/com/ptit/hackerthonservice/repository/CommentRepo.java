package com.ptit.hackerthonservice.repository;

import com.ptit.hackerthonservice.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
	@Query("SELECT c FROM Comment c LEFT JOIN c.replyTo rl LEFT JOIN c.exerciseComment ec LEFT JOIN c.solutionComment sc LEFT JOIN c.forumPostComment fc "
			+ "WHERE c.description LIKE :value AND ( :replyToId IS NULL OR rl.id = :replyToId ) "
			+ "AND ( :exerciseId IS NULL OR ec.exercise.id = :exerciseId ) "
			+ "AND ( :solutionId IS NULL OR sc.solution.id = :solutionId ) "
			+ "AND ( :forumId IS NULL OR fc.forumPost.id = :forumId ) "
			+ "AND ( :uid IS NULL OR (c.createdBy.id = :uid AND rl.id IS NULL))")
	Page<Comment> find(@Param("value") String value, @Param("replyToId") Long replyId, @Param("exerciseId") Long exerciseId,
					   @Param("solutionId") Long solutionId, @Param("forumId") Long forumId,
					   @Param("uid") Integer uid, Pageable pageable);
}
