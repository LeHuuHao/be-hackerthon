package com.ptit.hackerthonservice.aop;//package jmaster.io.hackerthon.aop;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.CacheManager;
//import org.springframework.stereotype.Component;
//
//import jmaster.io.hackerthon.dto.AnswerDTO;
//import jmaster.io.hackerthon.entity.Answer;
//import jmaster.io.hackerthon.repository.AnswerRepo;
//import jmaster.io.hackerthon.utils.CacheNames;
//
//import javax.persistence.NoResultException;
//import java.util.List;
//
//@Aspect
//@Component
//public class AnswerServiceAspect {
//    @Autowired
//    CacheManager cacheManager;
//
//    @Autowired
//    AnswerRepo answerRepo;
//
//    @Before("execution(* jmaster.io.hackerthon.service.AnswerService.create(*))")
//    public void create(JoinPoint joinPoint) {
//        cacheManager.getCache(CacheNames.CACHE_ANSWER_FIND).clear();
//        cacheManager.getCache(CacheNames.CACHE_ANSWER).clear();
//    }
//
//    @Before("execution(* jmaster.io.hackerthon.service.AnswerService.update(*))")
//    public void update(JoinPoint joinPoint) {
//        AnswerDTO ANSWERDTO = (AnswerDTO) joinPoint.getArgs()[0];
//        Answer currentUser = answerRepo.findById(ANSWERDTO.getId()).orElseThrow(NoResultException::new);
//
//        cacheManager.getCache(CacheNames.CACHE_ANSWER_FIND).clear();
//        cacheManager.getCache(CacheNames.CACHE_ANSWER).evict(currentUser.getId());
//    }
//
//    @Before("execution(* jmaster.io.hackerthon.service.AnswerService.delete(*))")
//    public void delete(JoinPoint joinPoint) {
//        int id = (Integer) joinPoint.getArgs()[0];
//        Answer currentUser = answerRepo.findById(id).orElseThrow(NoResultException::new);
//
//        cacheManager.getCache(CacheNames.CACHE_ANSWER_FIND).clear();
//        cacheManager.getCache(CacheNames.CACHE_ANSWER).evict(currentUser.getId());
//    }
//
//    @Before("execution(* jmaster.io.hackerthon.service.AnswerService.deleteAll(*))")
//    public void deleteAll(JoinPoint joinPoint) {
//        @SuppressWarnings("unchecked")
//        List<Integer> ids = (List<Integer>) joinPoint.getArgs()[0];
//
//        List<Answer> ANSWERs = answerRepo.findAllById(ids);
//
//        // clear cache
//        ANSWERs.forEach(ANSWER -> {
//            cacheManager.getCache(CacheNames.CACHE_ANSWER).evict(ANSWER.getId());
//        });
//        cacheManager.getCache(CacheNames.CACHE_ANSWER_FIND).clear();
//    }
//}
