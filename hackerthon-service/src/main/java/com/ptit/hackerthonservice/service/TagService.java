package com.ptit.hackerthonservice.service;


import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.dto.TagDTO;
import com.ptit.hackerthonservice.entity.Statistic;
import com.ptit.hackerthonservice.entity.Tag;
import com.ptit.hackerthonservice.repository.TagRepo;
import com.ptit.hackerthonservice.utils.CacheNames;
import com.ptit.hackerthonservice.utils.TagTypeEnum;
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

public interface TagService {
	void create(TagDTO tagDTO);

	void update(TagDTO tagDTO);

	void delete(Integer id);

	void deleteAll(List<Integer> ids);

	TagDTO get(Integer id);

	TagDTO finBySlug(String slug);

	ResponseDTO<List<TagDTO>> find(SearchDTO searchDTO);
}

@Service
class TagServiceImpl implements TagService {

	@Autowired
	TagRepo tagRepo;

	@Override
	@Transactional
	public void create(TagDTO tagDTO) {
		Tag tag = new ModelMapper().map(tagDTO, Tag.class);
		tag.setStatistic(new Statistic());

		tagRepo.save(tag);

		tagDTO.setId(tag.getId());
	}

	@Override
	@Transactional
	public void update(TagDTO tagDTO) {
		ModelMapper mapper = new ModelMapper();
		mapper.createTypeMap(TagDTO.class, Tag.class).addMappings(map -> {
			map.skip(Tag::setStatistic);
		}).setProvider(p -> tagRepo.findById(tagDTO.getId()).orElseThrow(NoResultException::new));

		Tag tag = mapper.map(tagDTO, Tag.class);

		tagRepo.save(tag);
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		tagRepo.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll(List<Integer> ids) {
		tagRepo.deleteAllById(ids);
	}

	@Override
	@Cacheable(cacheNames = CacheNames.CACHE_TAG, key = "#id")
	public TagDTO get(Integer id) {
		return tagRepo.findById(id).map(tag -> convert(tag)).orElseThrow(NoResultException::new);
	}

	@Override
	public TagDTO finBySlug(String slug) {
		return tagRepo.findBySlug(slug).map(tag -> convert(tag)).orElseThrow(NoResultException::new);
	}

	@Override
	public ResponseDTO<List<TagDTO>> find(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		TagTypeEnum tagType = null;

		if (StringUtils.hasText(searchDTO.getFilterBys().get("type"))) {
			tagType = TagTypeEnum.valueOf(String.valueOf(searchDTO.getFilterBys().get("type")));
		}

		Page<Tag> page = tagRepo.find(searchDTO.getValue(), tagType, pageable);

		@SuppressWarnings("unchecked")
		ResponseDTO<List<TagDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(tag -> convert(tag)).collect(Collectors.toList()));
		return responseDTO;
	}

	private TagDTO convert(Tag tag) {
		return new ModelMapper().map(tag, TagDTO.class);
	}
}
