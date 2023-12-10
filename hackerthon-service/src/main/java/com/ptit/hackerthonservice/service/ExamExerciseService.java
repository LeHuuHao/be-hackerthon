package com.ptit.hackerthonservice.service;

import com.ptit.hackerthonservice.dto.ExamExerciseDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.entity.Exam;
import com.ptit.hackerthonservice.entity.ExamExercise;
import com.ptit.hackerthonservice.entity.Exercise;
import com.ptit.hackerthonservice.repository.ExamExerciseRepo;
import com.ptit.hackerthonservice.repository.ExamRepo;
import com.ptit.hackerthonservice.repository.ExerciseRepo;
import com.ptit.hackerthonservice.utils.CacheNames;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ExamExerciseService {
    void create(ExamExerciseDTO examExerciseDTO);

    void update(ExamExerciseDTO examExerciseDTO);

    void delete(Integer id);

    void deleteAll(List<Integer> ids);

    ExamExerciseDTO get(Integer id);

    ResponseDTO<List<ExamExerciseDTO>> find(SearchDTO searchDTO);
}

@Service
class ExamExerciseServiceImpl implements  ExamExerciseService{

    @Autowired
    ExamExerciseRepo examExerciseRepo;

    @Autowired
    ExamRepo examRepo;

    @Autowired
    ExerciseRepo exerciseRepo;


    @Transactional
    @Override
    @CacheEvict(value = CacheNames.CACHE_EXAMEXERCISE_FIND,allEntries = true)
    public void create(ExamExerciseDTO examExerciseDTO) {
        Exam exam= examRepo.findById(examExerciseDTO.getExam().getId()).orElseThrow();
        Exercise exercise= exerciseRepo.findById(examExerciseDTO.getExercise().getId()).orElseThrow();
        ExamExercise examExercise= new ExamExercise();
        examExercise.setExam(exam);
        examExercise.setExercise(exercise);
        examExerciseRepo.save(examExercise);
        examExerciseDTO.setId(examExercise.getId());
    }

    @Transactional
    @Override
    @CacheEvict(value = CacheNames.CACHE_EXAMEXERCISE_FIND,allEntries = true)
    public void update(ExamExerciseDTO examExerciseDTO) {
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(ExamExerciseDTO.class, ExamExercise.class)
                .setProvider(p -> examExerciseRepo.findById(examExerciseDTO.getId()).orElseThrow(NoResultException::new));

        ExamExercise examExercise = mapper.map(examExerciseDTO, ExamExercise.class);
        examExerciseRepo.save(examExercise);
    }

    @Transactional
    @Override
    @CacheEvict(value = CacheNames.CACHE_EXAMEXERCISE_FIND,allEntries = true)
    public void delete(Integer id) {
        ExamExercise examExercise = examExerciseRepo.findById(id).orElseThrow(NoResultException::new);
        if(examExercise!= null){
            examExerciseRepo.deleteById(id);
        }
    }

    @Transactional
    @Override
    @CacheEvict(value = CacheNames.CACHE_EXAMEXERCISE_FIND,allEntries = true)
    public void deleteAll(List<Integer> ids) {
        examExerciseRepo.deleteAllByIdInBatch(ids);
    }

    @Cacheable(CacheNames.CACHE_EXAMEXERCISE)
    @Override
    public ExamExerciseDTO get(Integer id) {
        return examExerciseRepo.findById(id).map(examExercise -> convert(examExercise)).orElseThrow(NoResultException::new);
    }

    @Cacheable(cacheNames = CacheNames.CACHE_EXAMEXERCISE_FIND, unless = "#result.totalElements == 0", key = "#searchDTO.toString()")
    @Override
    public ResponseDTO<List<ExamExerciseDTO>> find(SearchDTO searchDTO) {
        List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
                .map(order -> {
                    if (order.getOrder().equals(SearchDTO.ASC))
                        return Sort.Order.asc(order.getProperty());

                    return Sort.Order.desc(order.getProperty());
                }).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

        Long examId=null;
        if (StringUtils.hasText(searchDTO.getFilterBys().get("examId"))) {
            examId = Long.valueOf(searchDTO.getFilterBys().get("examId"));
        }
        Page<ExamExercise> page = examExerciseRepo.find(searchDTO.getValue(),examId,pageable);

        ResponseDTO<List<ExamExerciseDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
        responseDTO.setData(page.get().map(examExercise -> convert(examExercise)).collect(Collectors.toList()));
        return responseDTO;
    }

    private ExamExerciseDTO convert(ExamExercise examExercise) {
        return new ModelMapper().map(examExercise, ExamExerciseDTO.class);
    }
}
