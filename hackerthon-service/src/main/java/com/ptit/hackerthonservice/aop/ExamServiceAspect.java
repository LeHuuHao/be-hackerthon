package com.ptit.hackerthonservice.aop;//package jmaster.io.hackerthon.aop;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.CacheManager;
//import org.springframework.stereotype.Component;
//
//import jmaster.io.hackerthon.dto.ExamDTO;
//import jmaster.io.hackerthon.entity.Exam;
//import jmaster.io.hackerthon.repository.ExamRepo;
//import jmaster.io.hackerthon.utils.CacheNames;
//
//import javax.persistence.NoResultException;
//import java.util.List;
//
//@Aspect
//@Component
//public class ExamServiceAspect {
//    @Autowired
//    CacheManager cacheManager;
//
//    @Autowired
//    ExamRepo examRepo;
//
//    @Before("execution(* jmaster.io.hackerthon.service.ExamService.create(*))")
//    public void create(JoinPoint joinPoint) {
//        cacheManager.getCache(CacheNames.CACHE_EXAM_FIND).clear();
//        cacheManager.getCache(CacheNames.CACHE_EXAM).clear();
//    }
//
//    @Before("execution(* jmaster.io.hackerthon.service.ExamService.update(*))")
//    public void update(JoinPoint joinPoint) {
//        ExamDTO examDTO = (ExamDTO) joinPoint.getArgs()[0];
//        Exam currentUser = examRepo.findById(examDTO.getId()).orElseThrow(NoResultException::new);
//
//        cacheManager.getCache(CacheNames.CACHE_EXAM_FIND).clear();
//        cacheManager.getCache(CacheNames.CACHE_EXAM).evict(currentUser.getId());
//    }
//
//    @Before("execution(* jmaster.io.hackerthon.service.ExamService.delete(*))")
//    public void delete(JoinPoint joinPoint) {
//        int id = (Integer) joinPoint.getArgs()[0];
//        Exam currentUser = examRepo.findById(id).orElseThrow(NoResultException::new);
//
//        cacheManager.getCache(CacheNames.CACHE_EXAM_FIND).clear();
//        cacheManager.getCache(CacheNames.CACHE_EXAM).evict(currentUser.getId());
//    }
//
//    @Before("execution(* jmaster.io.hackerthon.service.ExamService.deleteAll(*))")
//    public void deleteAll(JoinPoint joinPoint) {
//        @SuppressWarnings("unchecked")
//        List<Integer> ids = (List<Integer>) joinPoint.getArgs()[0];
//
//        List<Exam> exams = examRepo.findAllById(ids);
//
//        // clear cache
//        exams.forEach(exam -> {
//            cacheManager.getCache(CacheNames.CACHE_EXAM).evict(exam.getId());
//        });
//        cacheManager.getCache(CacheNames.CACHE_EXAM_FIND).clear();
//    }
//}
