//package com.ptit.hackerthonservice.aop;
//
//
//import com.ptit.hackerthonservice.dto.ExerciseDTO;
//import com.ptit.hackerthonservice.entity.Exercise;
//import com.ptit.hackerthonservice.repository.ExerciseRepo;
//import com.ptit.hackerthonservice.utils.CacheNames;
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
//public class ExerciseServiceAspect {
//	@Autowired
//	CacheManager cacheManager;
//
//	@Autowired
//	ExerciseRepo exerciseRepo;
//
//	@Before("execution(* jmaster.io.hackerthon.service.ExerciseService.create(*))")
//	public void create(JoinPoint joinPoint) {
//		ExerciseDTO exerciseDTO = (ExerciseDTO) joinPoint.getArgs()[0];
//		checkSecurity(exerciseDTO);
//
//		cacheManager.getCache(CacheNames.CACHE_EXERCISE_FIND).invalidate();
//	}
//
//	@Before("execution(* jmaster.io.hackerthon.service.ExerciseService.update(*))")
//	public void update(JoinPoint joinPoint) {
//		ExerciseDTO exerciseDTO = (ExerciseDTO) joinPoint.getArgs()[0];
//		checkSecurity(exerciseDTO);
//
//		Exercise exercise = exerciseRepo.findById(exerciseDTO.getId()).orElseThrow(NoResultException::new);
//		checkStatusSecurity(exercise);
//
//		cacheManager.getCache(CacheNames.CACHE_EXERCISE_FIND).invalidate();
//		cacheManager.getCache(CacheNames.CACHE_EXERCISE).evict(exerciseDTO.getId());
//	}
//
//	@Before("execution(* jmaster.io.hackerthon.service.ExerciseService.delete(*))")
//	public void delete(JoinPoint joinPoint) {
//		Long id = (Long) joinPoint.getArgs()[0];
//		Exercise exercise = exerciseRepo.findById(id).orElseThrow(NoResultException::new);
//		checkStatusSecurity(exercise);
//
//		cacheManager.getCache(CacheNames.CACHE_EXERCISE_FIND).invalidate();
//		cacheManager.getCache(CacheNames.CACHE_EXERCISE).evict(id);
//	}
//
//	@Before("execution(* jmaster.io.hackerthon.service.ExerciseService.deleteAll(*))")
//	public void deleteAll(JoinPoint joinPoint) {
//		@SuppressWarnings("unchecked")
//		List<Long> ids = (List<Long>) joinPoint.getArgs()[0];
//		List<Exercise> exercises = exerciseRepo.findAllById(ids);
//
//		// clear cache
//		exercises.forEach(exercise -> {
//			checkStatusSecurity(exercise);
//			cacheManager.getCache(CacheNames.CACHE_EXERCISE).evict(exercise.getId());
//		});
//		cacheManager.getCache(CacheNames.CACHE_EXERCISE_FIND).invalidate();
//	}
//
//	private void checkSecurity(ExerciseDTO exerciseDTO) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		List<String> authorities = auth.getAuthorities().stream().map(g -> g.getAuthority())
//				.collect(Collectors.toList());
//
//		if (authorities.contains(RoleEnum.EDITOR.getName()) && exerciseDTO.getStatus() == StatusEnum.ACTIVE)
//			throw new AccessDeniedException("No Permission");
//	}
//
//	private void checkStatusSecurity(Exercise exercise) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		List<String> authorities = auth.getAuthorities().stream().map(g -> g.getAuthority())
//				.collect(Collectors.toList());
//
//		if (authorities.contains(RoleEnum.EDITOR.getName()) && (exercise.getStatus() == StatusEnum.ACTIVE
//				|| !exercise.getCreatedBy().getId().equals(auth.getName())))
//			throw new AccessDeniedException("No Permission");
//	}
//}
