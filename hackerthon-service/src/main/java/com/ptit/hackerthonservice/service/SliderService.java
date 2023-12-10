package com.ptit.hackerthonservice.service;

import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.dto.SliderDTO;
import com.ptit.hackerthonservice.entity.Slider;
import com.ptit.hackerthonservice.repository.SliderRepository;
import com.ptit.hackerthonservice.utils.CacheNames;
import com.ptit.hackerthonservice.utils.StatusEnum;
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

public interface SliderService {
	void create(SliderDTO sliderDTO);

	void update(SliderDTO sliderDTO);

	void delete(int id);

	void deleteAll(List<Integer> ids);

	SliderDTO get(int id);

	ResponseDTO<List<SliderDTO>> find(SearchDTO searchDTO);
}

@Service
class SliderServiceImpl implements SliderService {
	@Autowired
	SliderRepository sliderRepository;

	@Override
	@Transactional
	@CacheEvict(cacheNames = CacheNames.CACHE_SLIDERS, allEntries = true)
	public void create(SliderDTO sliderDTO) {
		Slider slider = new ModelMapper().map(sliderDTO, Slider.class);
		sliderRepository.save(slider);
		sliderDTO.setId(slider.getId());
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = CacheNames.CACHE_SLIDERS, allEntries = true)
	public void update(SliderDTO sliderDTO) {
		Slider slider = new ModelMapper().map(sliderDTO, Slider.class);
		sliderRepository.save(slider);
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = CacheNames.CACHE_SLIDERS, allEntries = true)
	public void delete(int id) {
		sliderRepository.deleteById(id);
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = CacheNames.CACHE_SLIDERS, allEntries = true)
	public void deleteAll(List<Integer> ids) {
		sliderRepository.deleteAllById(ids);
	}

	@Override
	public SliderDTO get(int id) {
		return sliderRepository.findById(id).map(role -> convert(role)).orElseThrow(NoResultException::new);
	}

	@Cacheable(cacheNames = CacheNames.CACHE_SLIDERS, unless = "#result.totalElements == 0", key = "#searchDTO.toString()")
	@Override
	public ResponseDTO<List<SliderDTO>> find(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		StatusEnum statusEnum = null;
		if (StringUtils.hasText(searchDTO.getFilterBys().get("status"))) {
			statusEnum = StatusEnum.valueOf(String.valueOf(searchDTO.getFilterBys().get("status")));
		}

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		Page<Slider> page = sliderRepository.find(searchDTO.getValue(), statusEnum, pageable);

		@SuppressWarnings("unchecked")
		ResponseDTO<List<SliderDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(slider -> convert(slider)).collect(Collectors.toList()));
		return responseDTO;
	}

	private SliderDTO convert(Slider slider) {
		return new ModelMapper().map(slider, SliderDTO.class);
	}
}
