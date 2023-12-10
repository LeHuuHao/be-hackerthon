package com.ptit.hackerthonservice.service;


import com.ptit.hackerthonservice.dto.CommentDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.entity.Comment;
import com.ptit.hackerthonservice.entity.ExerciseComment;
import com.ptit.hackerthonservice.entity.ForumPostComment;
import com.ptit.hackerthonservice.entity.SolutionComment;
import com.ptit.hackerthonservice.repository.CommentRepo;
import com.ptit.hackerthonservice.repository.ExerciseRepo;
import com.ptit.hackerthonservice.repository.ForumPostRepository;
import com.ptit.hackerthonservice.repository.SolutionRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.NoResultException;
import java.util.*;
import java.util.stream.Collectors;

public interface CommentService {
	void create(CommentDTO commentDTO);

	void update(CommentDTO commentDTO);

	void updateLike(long id);

	void delete(long id);

	void deleteAll(List<Long> ids);

	ResponseDTO<List<CommentDTO>> find(SearchDTO searchDTO);
}

@Service
class CommentServiceImpl implements CommentService {

	@Autowired
	CommentRepo commentRepository;

	@Autowired
	ExerciseRepo exerciseRepository;

	@Autowired
	SolutionRepository solutionRepository;

	@Autowired
	ForumPostRepository forumPostRepository;


	@Transactional
	@Override
	public void create(CommentDTO commentDTO) {
		Comment comment = new ModelMapper().map(commentDTO, Comment.class);
		if (commentDTO.getReplyToId() != null) {
			Comment parentComment = commentRepository.findById(commentDTO.getReplyToId()).orElseThrow();
			parentComment.setReplyNo(parentComment.getReplyNo() + 1);
			commentRepository.save(parentComment);

			comment.setReplyTo(parentComment);
		} else if (commentDTO.getExerciseId() != null) {
			ExerciseComment exerciseComment = new ExerciseComment();
			exerciseComment.setComment(comment);
			exerciseComment.setExercise(exerciseRepository.findById(commentDTO.getExerciseId()).orElseThrow());
			comment.setExerciseComment(exerciseComment);
		} else if (commentDTO.getSolutionId() != null) {
			SolutionComment solutionComment = new SolutionComment();
			solutionComment.setComment(comment);
			solutionComment.setSolution(solutionRepository.findById(commentDTO.getSolutionId()).orElseThrow());
			comment.setSolutionComment(solutionComment);

		}
		else if (commentDTO.getForumId() != null) {
			ForumPostComment forumPostComment = new ForumPostComment();
			forumPostComment.setComment(comment);
			forumPostComment.setForumPost(forumPostRepository.findById(commentDTO.getForumId()).orElseThrow());
			comment.setForumPostComment(forumPostComment);

		}
		else
			throw new NoSuchElementException();

		commentRepository.save(comment);
		commentDTO.setId(comment.getId());
	}

	@Transactional
	@Override
	public void update(CommentDTO commentDTO) {
		Comment comment = commentRepository.findById(commentDTO.getId()).orElseThrow(NoResultException :: new);

		comment.setDescription(commentDTO.getDescription());
		commentRepository.save(comment);
	}

	@Override
	@Transactional
	public void updateLike(long id) {
		Comment comment = commentRepository.findById(id).orElseThrow();
		comment.setLikeNo(comment.getLikeNo() + 1);
		commentRepository.save(comment);
	}

	@Transactional
	@Override
	public void delete(long id) {
		Comment comment = commentRepository.findById(id).orElseThrow().getReplyTo();
		commentRepository.deleteById(id);
		if (comment != null) {
			comment.setReplyNo(comment.getReplyNo() - 1);
			commentRepository.save(comment);
		}
	}

	@Transactional
	@Override
	public void deleteAll(List<Long> ids) {
		List<Comment> comments = commentRepository.findAllById(ids).stream().map(Comment :: getReplyTo)
				.filter(Objects:: nonNull).collect(Collectors.toList());
		commentRepository.deleteAllById(ids);

		// update
		comments.forEach(c -> {
			c.setReplyNo(c.getReplyNo() - 1);
			commentRepository.save(c);
		});
	}

	@Override
	public ResponseDTO<List<CommentDTO>> find(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections :: emptyList)
				.stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		Long replyToId = null;
		if (StringUtils.hasText(searchDTO.getFilterBys().get("replyToId"))) {
			replyToId = Long.valueOf(searchDTO.getFilterBys().get("replyToId"));
		}

		Long exerciseId = null;
		if (StringUtils.hasText(searchDTO.getFilterBys().get("exerciseId"))) {
			exerciseId = Long.valueOf(searchDTO.getFilterBys().get("exerciseId"));
		}

		Long solutionId = null;
		if (StringUtils.hasText(searchDTO.getFilterBys().get("solutionId"))) {
			solutionId = Long.valueOf(searchDTO.getFilterBys().get("solutionId"));
		}
		Long forumId = null;
		if (StringUtils.hasText(searchDTO.getFilterBys().get("forumId"))) {
			forumId = Long.valueOf(searchDTO.getFilterBys().get("forumId"));
		}


		Integer createdById = null;
		if (StringUtils.hasText(searchDTO.getFilterBys().get("createdById"))) {
			createdById = Integer.parseInt(searchDTO.getFilterBys().get("createdById"));
		}

		Page<Comment> page = commentRepository.find(searchDTO.getValue(), replyToId, exerciseId, solutionId,forumId, createdById, pageable);

		@SuppressWarnings("unchecked")
		ResponseDTO<List<CommentDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(this :: convert).collect(Collectors.toList()));
		return responseDTO;
	}

	private CommentDTO convert(Comment comment) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return mapper.map(comment, CommentDTO.class);
	}
}