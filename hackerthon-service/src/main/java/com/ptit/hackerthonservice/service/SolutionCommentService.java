package com.ptit.hackerthonservice.service;


import com.ptit.hackerthonservice.dto.*;
import com.ptit.hackerthonservice.entity.Comment;
import com.ptit.hackerthonservice.entity.Solution;
import com.ptit.hackerthonservice.entity.SolutionComment;
import com.ptit.hackerthonservice.repository.SolutionCommentRepository;
import com.ptit.hackerthonservice.repository.SolutionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

public interface SolutionCommentService {
    void create(SolutionCommentDTO solutionCommentDTO);

    void update(SolutionCommentDTO solutionCommentDTO);

    void delete(long id);

    void deleteAll(List<Long> ids);

    SolutionCommentDTO get(long id);

    ResponseDTO<List<SolutionCommentDTO>> find(SearchDTO searchDTO);
}

@Service
class SolutionCommentServiceImpl implements SolutionCommentService {

    @Autowired
    SolutionCommentRepository solutionCommentRepository;

    @Autowired
    SolutionRepository solutionRepository;

    @Transactional
    @Override
    public void create(SolutionCommentDTO solutionCommentDTO) {
        ModelMapper mapper = new ModelMapper();
        SolutionComment solutionComment = new SolutionComment();
        Comment comment= new Comment();
        comment.setDescription(solutionCommentDTO.getComment().getDescription());
        Solution solution=solutionRepository.findById(solutionCommentDTO.getSolution().getId()).orElseThrow();
        solutionComment.setComment(comment);
        solutionComment.setSolution(solution);
        solutionCommentRepository.save(solutionComment);
        solutionCommentDTO.setCommentId(solutionComment.getCommentId());
    }

    @Transactional
    @Override
    public void update(SolutionCommentDTO solutionCommentDTO) {
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(SolutionCommentDTO.class, SolutionComment.class).setProvider(p -> solutionCommentRepository.findByCommentId(solutionCommentDTO.getCommentId()).orElseThrow(NoResultException::new));

        SolutionComment solutionComment = mapper.map(solutionCommentDTO, SolutionComment.class);
        solutionCommentRepository.save(solutionComment);
    }

    @Transactional
    @Override
    public void delete(long id) {
        SolutionComment solutionComment = solutionCommentRepository.findById(id).orElseThrow(NoResultException::new);
        if (solutionComment != null) {
            solutionCommentRepository.deleteById(id);
        }
    }

    @Transactional
    @Override
    public void deleteAll(List<Long> ids) {
        solutionCommentRepository.deleteAllById(ids);
    }

    @Override
    public SolutionCommentDTO get(long id) {
        return solutionCommentRepository.findById(id).map(comment -> convert(comment)).orElseThrow(NoResultException::new);
    }

    @Override
    public ResponseDTO<List<SolutionCommentDTO>> find(SearchDTO searchDTO) {
        List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
                .map(order -> {
                    if (order.getOrder().equals(SearchDTO.ASC))
                        return Sort.Order.asc(order.getProperty());

                    return Sort.Order.desc(order.getProperty());
                }).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

        Long solutionId = null;
        if (StringUtils.hasText(searchDTO.getFilterBys().get("solutionId"))) {
            solutionId = Long.valueOf(searchDTO.getFilterBys().get("solutionId"));
        }

        Page<SolutionComment> page = solutionCommentRepository.find(searchDTO.getValue(),solutionId, pageable);

        @SuppressWarnings("unchecked")
        ResponseDTO<List<SolutionCommentDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
        responseDTO.setData(page.get().map(comment -> convert(comment)).collect(Collectors.toList()));
        return responseDTO;
    }

    private SolutionCommentDTO convert(SolutionComment solutionComment) {
        ModelMapper modelMapper= new ModelMapper();
        SolutionCommentDTO solutionCommentDTO= new SolutionCommentDTO();
        solutionCommentDTO.setCommentId(solutionComment.getCommentId());
        solutionCommentDTO.setComment(modelMapper.map(solutionComment.getComment(), CommentDTO.class));
        solutionCommentDTO.setSolution(modelMapper.map(solutionComment.getSolution(), SolutionDTO.class));
        return solutionCommentDTO;
    }
}
