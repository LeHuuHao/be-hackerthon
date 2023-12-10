package com.ptit.hackerthonservice.service;

import com.github.slugify.Slugify;
import com.ptit.hackerthonservice.dto.ForumPostDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.entity.ForumPost;
import com.ptit.hackerthonservice.entity.Statistic;
import com.ptit.hackerthonservice.repository.ForumPostRepository;
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

public interface ForumPostService {

    void create(ForumPostDTO forumPostDTO);

    void update(ForumPostDTO forumPostDTO);

    void delete(Long id);

    void deleteAll(List<Long> ids);

    ForumPostDTO get(Long id);

    ForumPostDTO finBySlug(String slug);

    ResponseDTO<List<ForumPostDTO>> find(SearchDTO searchDTO);


}

@Service
class ForumPostServiceImpl implements ForumPostService {

    @Autowired
    ForumPostRepository forumPostRepository;


    @Transactional
    @Override
    public void create(ForumPostDTO forumPostDTO) {
        final Slugify slugify = Slugify.builder().locale(Locale.forLanguageTag("vi")).build();
        String slug = slugify.slugify(forumPostDTO.getTitle());
        while (true) {
            ForumPost forumPost = forumPostRepository.findBySlug(slug).orElse(null);
            if (forumPost != null)
                slug = slug + "-" + System.currentTimeMillis();
            else
                break;
        }
        forumPostDTO.setSlug(slug);

        // copy
        ForumPost forumPost = new ModelMapper().map(forumPostDTO, ForumPost.class);

        forumPost.setStatistic(new Statistic());
        forumPostRepository.save(forumPost);
        forumPostDTO.setId(forumPost.getId());
    }

    @Transactional
    @Override
    public void update(ForumPostDTO forumPostDTO) {
        final ForumPost forumPost = forumPostRepository.findById(forumPostDTO.getId())
                .orElseThrow(NoResultException::new);

        // gen slug for solution
        if (!forumPost.getTitle().equals(forumPostDTO.getTitle())) {
            final Slugify slugify = Slugify.builder().locale(Locale.forLanguageTag("vi")).build();
            String slug = slugify.slugify(forumPostDTO.getTitle());
            while (true) {
                ForumPost s = forumPostRepository.findBySlug(slug).orElse(null);
                if (s != null)
                    slug = slug + "-" + System.currentTimeMillis();
                else
                    break;
            }
            forumPostDTO.setSlug(slug);
        } else
            forumPostDTO.setSlug(forumPost.getSlug());

        // copy
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ForumPost saveSolution = mapper.createTypeMap(ForumPostDTO.class, ForumPost.class)
                .setProvider(p -> forumPost).map(forumPostDTO);

        forumPostRepository.save(saveSolution);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        ForumPost forumPost = forumPostRepository.findById(id).orElseThrow(NoResultException::new);
        if (forumPost != null) {
            forumPostRepository.deleteById(id);
        }
    }

    @Transactional
    @Override
    public void deleteAll(List<Long> ids) {
        forumPostRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public ForumPostDTO get(Long id) {
        return forumPostRepository.findById(id).map(forumPost -> convert(forumPost)).orElseThrow(NoResultException::new);
    }

    @Override
    public ResponseDTO<List<ForumPostDTO>> find(SearchDTO searchDTO) {
        List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList)
                .stream()
                .map(order -> {
                    if (order.getOrder().equals(SearchDTO.ASC))
                        return Sort.Order.asc(order.getProperty());

                    return Sort.Order.desc(order.getProperty());
                }).collect(Collectors.toList());

        Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));


        Integer createdById = null;
        if (StringUtils.hasText(searchDTO.getFilterBys().get("createdById"))) {
            createdById = Integer.valueOf(searchDTO.getFilterBys().get("createdById"));
        }

        List<Integer> tagIds = null;
        if (StringUtils.hasText(searchDTO.getFilterBys().get("tagIds"))) {
            String[] tagArr = searchDTO.getFilterBys().get("tagIds").split(",");
            tagIds = Arrays.stream(tagArr).map(Integer::valueOf).collect(Collectors.toList());
        }


        Page<ForumPost> page = forumPostRepository.find(searchDTO.getValue(), createdById, tagIds, pageable);

        @SuppressWarnings("unchecked")
        ResponseDTO<List<ForumPostDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
        responseDTO.setData(page.get().map(this::convert).collect(Collectors.toList()));
        return responseDTO;
    }

    @Override
    public ForumPostDTO finBySlug(String slug) {
        return forumPostRepository.findBySlug(slug).map(this::convert).orElseThrow();
    }


    private ForumPostDTO convert(ForumPost forumPost) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ForumPostDTO forumPostDTO = mapper.map(forumPost, ForumPostDTO.class);
        return forumPostDTO;
    }


}
