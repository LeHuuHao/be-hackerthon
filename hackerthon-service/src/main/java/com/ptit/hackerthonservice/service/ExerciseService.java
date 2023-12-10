package com.ptit.hackerthonservice.service;


import com.ptit.hackerthonservice.dto.ExerciseDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.dto.TestCaseDTO;
import com.ptit.hackerthonservice.entity.Exercise;
import com.ptit.hackerthonservice.entity.Statistic;
import com.ptit.hackerthonservice.entity.TestCase;
import com.ptit.hackerthonservice.repository.ExerciseRepo;
import com.ptit.hackerthonservice.repository.TagRepo;
import com.ptit.hackerthonservice.utils.CacheNames;
import com.ptit.hackerthonservice.utils.RoleEnum;
import com.ptit.hackerthonservice.utils.StatusEnum;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.NoResultException;
import java.util.*;
import java.util.stream.Collectors;

public interface ExerciseService {
    void create(ExerciseDTO exerciseDTO);

    void update(ExerciseDTO exerciseDTO);

    void delete(Long id);

    void deleteAll(List<Long> ids);

    ExerciseDTO get(Long id);

    ResponseDTO<List<ExerciseDTO>> find(SearchDTO searchDTO);

    ExerciseDTO finBySlug(String slug);
}

@Service
class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    ExerciseRepo exerciseRepo;

    @Autowired
    TagRepo tagRepo;

    @Override
    @Transactional
    public void create(ExerciseDTO exerciseDTO) {
        Exercise exercise = new ModelMapper().map(exerciseDTO, Exercise.class);
        exercise.setStatistic(new Statistic());
        exercise.getTestCases().forEach(testcase -> testcase.setExercise(exercise));

        exerciseRepo.save(exercise);

        exerciseDTO.setId(exercise.getId());
    }

    @Override
    @Transactional
    public void update(ExerciseDTO exerciseDTO) {
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(ExerciseDTO.class, Exercise.class).addMappings(map -> {
            map.skip(Exercise::setTestCases);
            map.skip(Exercise::setStatistic);
        }).setProvider(p -> exerciseRepo.findById(exerciseDTO.getId()).orElseThrow(NoResultException::new));

        Exercise exercise = mapper.map(exerciseDTO, Exercise.class);

        Iterator<TestCase> iterator = exercise.getTestCases().iterator();

        // update old one
        while (iterator.hasNext()) {
            TestCase testCase = iterator.next();

            TestCaseDTO testCaseDTO = exerciseDTO.getTestCases().stream().filter(t -> t.getId() != null && testCase.getId().longValue() == t.getId()).findFirst().orElse(null);

            if (testCaseDTO == null) {
                iterator.remove();
            } else {
                testCase.setInput(testCaseDTO.getInput());
                testCase.setExpectedOutput(testCaseDTO.getExpectedOutput());
                testCase.setScore(testCaseDTO.getScore());
            }
        }
        // add new test case
        exerciseDTO.getTestCases().stream().filter(testCaseDTO -> testCaseDTO.getId() == null).forEach(testCaseDTO -> {
            TestCase testCase = new ModelMapper().map(testCaseDTO, TestCase.class);
            testCase.setExercise(exercise);
            exercise.getTestCases().add(testCase);
        });

        exerciseRepo.save(exercise);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        exerciseRepo.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll(List<Long> ids) {
        exerciseRepo.deleteAllById(ids);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.CACHE_EXERCISE, key = "#id")
    public ExerciseDTO get(Long id) {
        Set<String> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(i -> i.getAuthority()).collect(Collectors.toSet());

        return exerciseRepo.findById(id).map(exercise -> convert(exercise, authorities)).orElseThrow(NoResultException::new);
    }

    @Override
    public ResponseDTO<List<ExerciseDTO>> find(SearchDTO searchDTO) {
        List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream().map(order -> {
            if (order.getOrder().equals(SearchDTO.ASC)) return Sort.Order.asc(order.getProperty());

            return Sort.Order.desc(order.getProperty());
        }).collect(Collectors.toList());

        List<Integer> tagIds = null;
        if (StringUtils.hasText(searchDTO.getFilterBys().get("tagIds"))) {
            String[] tagArr = searchDTO.getFilterBys().get("tagIds").split(",");
            tagIds = Arrays.asList(tagArr).stream().map(item -> Integer.valueOf(item)).collect(Collectors.toList());
        }

        StatusEnum statusEnum = null;
        if (StringUtils.hasText(searchDTO.getFilterBys().get("status"))) {
            statusEnum = StatusEnum.valueOf(String.valueOf(searchDTO.getFilterBys().get("status")));
        }

        // create user id
        String createdBy = null;
        if (StringUtils.hasText(searchDTO.getFilterBys().get("createdBy"))) {
            createdBy = searchDTO.getFilterBys().get("createdBy");
        }
        // authorities
        final Set<String> authorities = Set.of(searchDTO.getFilterBys().get("authorities").split(","));

        Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));
        Page<Exercise> page = exerciseRepo.find(searchDTO.getValue(), tagIds, statusEnum, createdBy, pageable);

        @SuppressWarnings("unchecked") ResponseDTO<List<ExerciseDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);

        responseDTO.setData(page.get().map(exercise -> convert(exercise, authorities)).collect(Collectors.toList()));

        return responseDTO;
    }

    private ExerciseDTO convert(Exercise exercise, Set<String> authorities) {
        if (authorities.contains(RoleEnum.MANAGER.getRoleName()) || authorities.contains(RoleEnum.EDITOR.getRoleName()) || authorities.contains(RoleEnum.ADMIN.getRoleName())){
             ModelMapper mapper = new ModelMapper();
             ExerciseDTO exerciseDTO=mapper.map(exercise, ExerciseDTO.class);
			return exerciseDTO;
		}


        return new ModelMapper().createTypeMap(Exercise.class, ExerciseDTO.class).addMappings(map -> {
            map.skip(ExerciseDTO::setTestCases);
            map.skip(ExerciseDTO::setSourcecode);
        }).setPostConverter(convert -> {
            // get one test case
            TestCaseDTO testCaseDTO = convert.getSource().getTestCases().stream().findFirst().map(tc -> new ModelMapper().map(tc, TestCaseDTO.class)).orElse(null);
            convert.getDestination().setTestCases(Collections.singletonList(testCaseDTO));

            return convert.getDestination();
        }).map(exercise);

    }


    @Override
    @Transactional
    public ExerciseDTO finBySlug(String slug) {
        Set<String> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(i -> i.getAuthority()).collect(Collectors.toSet());

        return exerciseRepo.findBySlug(slug).map(exercise -> convert(exercise, authorities)).orElseThrow(NoResultException::new);
    }
}
