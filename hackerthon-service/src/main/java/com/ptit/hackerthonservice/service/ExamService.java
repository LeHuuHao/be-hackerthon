package com.ptit.hackerthonservice.service;


import com.ptit.hackerthonservice.dto.ExamDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.dto.StatisticUserDTO;
import com.ptit.hackerthonservice.entity.Exam;
import com.ptit.hackerthonservice.repository.ExamRepo;
import com.ptit.hackerthonservice.utils.CacheNames;
import com.ptit.hackerthonservice.utils.DateTimeUtils;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ExamService {
    void create(ExamDTO examDTO);

    void update(ExamDTO examDTO);

    void updateEnd(ExamDTO examDTO);

    void delete(Long id);

    void deleteAll(List<Long> ids);

    ExamDTO get(Long id);

    ResponseDTO<List<ExamDTO>> find(SearchDTO searchDTO);

    ExamDTO finBySlug(String slug);


}

@Service
class ExamServiceImpl implements ExamService {

    @Autowired
    ExamRepo examRepo;

    @Transactional
    @Override
    @CacheEvict(value = CacheNames.CACHE_EXAM_FIND, allEntries = true)
    public void create(ExamDTO examDTO) {
		examDTO.setStartAt(cutTime(examDTO.getStartAt()));
		examDTO.setStartAt(cutTime(examDTO.getEndAt()));
        Exam exam = new ModelMapper().map(examDTO, Exam.class);
        examRepo.save(exam);
        examDTO.setId(exam.getId());
    }

    @Transactional
    @Override
    @CacheEvict(value = CacheNames.CACHE_EXAM_FIND, allEntries = true)
    public void update(ExamDTO examDTO) {
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(ExamDTO.class, Exam.class)
                .setProvider(p -> examRepo.findById(examDTO.getId()).orElseThrow(NoResultException::new));
        examDTO.setStartAt(cutTime(examDTO.getStartAt()));
        examDTO.setStartAt(cutTime(examDTO.getEndAt()));
        Exam exam = mapper.map(examDTO, Exam.class);

        examRepo.save(exam);
    }

    @Transactional
    @Override
    @CacheEvict(value = CacheNames.CACHE_EXAM_FIND, allEntries = true)
    public void updateEnd(ExamDTO examDTO) {
        ModelMapper mapper = new ModelMapper();
         examDTO.setEndTime(true);
        Exam exam = mapper.map(examDTO, Exam.class);

        examRepo.save(exam);
    }

    @Transactional
    @Override
    @CacheEvict(value = CacheNames.CACHE_EXAM_FIND, allEntries = true)
    public void delete(Long id) {
        examRepo.deleteById(id);
    }

    @Transactional
    @Override
    @CacheEvict(value = CacheNames.CACHE_EXAM_FIND, allEntries = true)
    public void deleteAll(List<Long> ids) {
        examRepo.deleteAllById(ids);
    }

    @Cacheable(CacheNames.CACHE_EXAM)
    @Override
    public ExamDTO get(Long id) {
        return examRepo.findById(id).map(exam -> convert(exam)).orElseThrow(NoResultException::new);
    }

    @Cacheable(cacheNames = CacheNames.CACHE_EXAM_FIND, unless = "#result.totalElements == 0", key = "#searchDTO.toString()")
    @Override
    public ResponseDTO<List<ExamDTO>> find(SearchDTO searchDTO) {
        List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
                .map(order -> {
                    if (order.getOrder().equals(SearchDTO.ASC))
                        return Sort.Order.asc(order.getProperty());

                    return Sort.Order.desc(order.getProperty());
                }).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

        Page<Exam> page = examRepo.find(searchDTO.getValue(), pageable);

        @SuppressWarnings("unchecked")
        ResponseDTO<List<ExamDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
        responseDTO.setData(page.get().map(exam -> convert(exam)).collect(Collectors.toList()));
        return responseDTO;
    }



    private ExamDTO convert(Exam exam) {
        return new ModelMapper().map(exam, ExamDTO.class);
    }

    private String cutTime(String time) {
        String[] parts = time.split(" ");
        if (parts.length == 4) {
            String datePart = parts[0];
            String timePart = parts[1] + " " + parts[3];

            String formattedDate = datePart + "  " + timePart;
            return formattedDate;
        }

        return "";
    }


    @Override
    @Transactional
    public ExamDTO finBySlug(String slug) {
        return examRepo.findBySlug(slug).map(exam -> convert(exam))
                .orElseThrow(NoResultException::new);
    }
}
