//package com.ptit.hackerthonservice.aop;
//
//import com.ptit.hackerthonservice.dto.CommentDTO;
//import com.ptit.hackerthonservice.entity.Comment;
//import com.ptit.hackerthonservice.repository.CommentRepo;
//import com.ptit.hackerthonservice.utils.CacheNames;
//import com.ptit.hackerthonservice.utils.RoleEnum;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.CacheManager;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import javax.persistence.NoResultException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Aspect
//@Component
//public class CommentServiceAspect {
//	@Autowired
//	CacheManager cacheManager;
//
//	@Autowired
//	CommentRepo commentRepo;
//
//	@Before("execution(* jmaster.io.hackerthon.service.CommentService.update(*))")
//	public void update(JoinPoint joinPoint) {
//		CommentDTO commentDTO = (CommentDTO) joinPoint.getArgs()[0];
//		Comment comment = commentRepo.findById(commentDTO.getId()).orElseThrow(NoResultException::new);
//
//		checkSecurity(comment);
//
//		cacheManager.getCache(CacheNames.CACHE_COMMENT_FIND).clear();
//	}
//
//	@Before("execution(* jmaster.io.hackerthon.service.CommentService.delete(*))")
//	public void delete(JoinPoint joinPoint) {
//		Long id = (Long) joinPoint.getArgs()[0];
//		Comment comment = commentRepo.findById(id).orElseThrow(NoResultException::new);
//
//		checkSecurity(comment);
//
//		cacheManager.getCache(CacheNames.CACHE_COMMENT_FIND).clear();
//	}
//
//	@Before("execution(* jmaster.io.hackerthon.service.CommentService.deleteAll(*))")
//	public void deleteAll(JoinPoint joinPoint) {
//		@SuppressWarnings("unchecked")
//		List<Long> ids = (List<Long>) joinPoint.getArgs()[0];
//
//		List<Comment> comments = commentRepo.findAllById(ids);
//
//		comments.forEach(comment -> {
//			checkSecurity(comment);
//		});
//
//		cacheManager.getCache(CacheNames.CACHE_COMMENT_FIND).clear();
//	}
//
//	private void checkSecurity(Comment comment) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		List<String> authorities = auth.getAuthorities().stream().map(g -> g.getAuthority())
//				.collect(Collectors.toList());
//
//		if (!authorities.contains(RoleEnum.ADMIN.getName()) && !authorities.contains(RoleEnum.MANAGER.getName())
//				&& !comment.getCreatedBy().getId().equals(auth.getName()))
//			throw new AccessDeniedException("No Permission");
//	}
//}
