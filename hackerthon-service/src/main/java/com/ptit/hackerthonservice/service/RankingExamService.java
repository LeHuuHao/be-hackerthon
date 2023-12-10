package com.ptit.hackerthonservice.service;

import com.ptit.hackerthonservice.dto.ExamExerciseDTO;
import com.ptit.hackerthonservice.dto.RankingExamDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.entity.Exam;
import com.ptit.hackerthonservice.entity.ExamExercise;
import com.ptit.hackerthonservice.entity.Exercise;
import com.ptit.hackerthonservice.entity.RankingExam;
import com.ptit.hackerthonservice.repository.ExamExerciseRepo;
import com.ptit.hackerthonservice.repository.ExamRepo;
import com.ptit.hackerthonservice.repository.ExerciseRepo;
import com.ptit.hackerthonservice.repository.RankingExamRepository;
import com.ptit.hackerthonservice.utils.CacheNames;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

public interface RankingExamService {
    void create(RankingExamDTO rankingExamDTO);

    void update(RankingExamDTO rankingExamDTO);

    void delete(Long id);

    void deleteAll(List<Long> ids);

    RankingExamDTO get(Long id);

    RankingExamDTO check(Integer id);

    ResponseDTO<List<RankingExamDTO>> find(SearchDTO searchDTO);
}

@Service
class RankingExamServiceImpl implements  RankingExamService{

    @Autowired
    RankingExamRepository rankingExamRepository;

    @Autowired
    ExamRepo examRepo;


    @Transactional
    @Override
    public void create(RankingExamDTO rankingExamDTO) {
        Exam exam= examRepo.findById(rankingExamDTO.getExam().getId()).orElseThrow();
        RankingExam rankingExam= new RankingExam();
        rankingExam.setExam(exam);
        rankingExam.setScore(rankingExamDTO.getScore());
        rankingExamRepository.save(rankingExam);
        rankingExamDTO.setId(rankingExam.getId());
    }

    @Transactional
    @Override
    public void update(RankingExamDTO rankingExamDTO) {
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(RankingExamDTO.class, RankingExam.class)
                .setProvider(p -> rankingExamRepository.findById(rankingExamDTO.getId()).orElseThrow(NoResultException::new));

        RankingExam rankingExam = mapper.map(rankingExamDTO, RankingExam.class);
        rankingExamRepository.save(rankingExam);
    }



    @Transactional
    @Override
    public void delete(Long id) {
        RankingExam rankingExam = rankingExamRepository.findById(id).orElseThrow(NoResultException::new);
        if(rankingExam!= null){
            rankingExamRepository.deleteById(id);
        }
    }

    @Transactional
    @Override
    public void deleteAll(List<Long> ids) {
        rankingExamRepository.deleteAllByIdInBatch(ids);
    }

    @Cacheable(CacheNames.CACHE_RANKING_EXAM)
    @Override
    public RankingExamDTO get(Long id) {
        return rankingExamRepository.findById(id).map(rankingExam -> convert(rankingExam)).orElseThrow(NoResultException::new);
    }

    @Override
    public RankingExamDTO check(Integer uid) {
        RankingExam rankingExam = rankingExamRepository.checkUser(uid).orElseGet(() -> {
            return null;
        });
        if(rankingExam==null){
            return  null;
        }
        return convert(rankingExam);
    }

    @Override
    public ResponseDTO<List<RankingExamDTO>> find(SearchDTO searchDTO) {
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
        Page<RankingExam> page = rankingExamRepository.find(searchDTO.getValue(),examId,pageable);

        ResponseDTO<List<RankingExamDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
        responseDTO.setData(page.get().map(examExercise -> convert(examExercise)).collect(Collectors.toList()));
        return responseDTO;
    }

    private RankingExamDTO convert(RankingExam rankingExam) {
        return new ModelMapper().map(rankingExam, RankingExamDTO.class);
    }
}