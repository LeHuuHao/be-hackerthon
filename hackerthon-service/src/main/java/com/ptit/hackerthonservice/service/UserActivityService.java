package com.ptit.hackerthonservice.service;


import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.dto.UserActivityDTO;
import com.ptit.hackerthonservice.entity.UserActivity;
import com.ptit.hackerthonservice.repository.UserActivityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface UserActivityService {
	ResponseDTO<List<UserActivityDTO>> find(SearchDTO searchDTO);
}

@Service
class UserActivityServiceImpl implements UserActivityService {
	@Autowired
	UserActivityRepository userActivityRepository;

	@Override
	@SuppressWarnings("unchecked")
	public ResponseDTO<List<UserActivityDTO>> find(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		Page<UserActivity> page = userActivityRepository.find(searchDTO.getValue(), pageable);

		ResponseDTO<List<UserActivityDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(userActivity -> convert(userActivity)).collect(Collectors.toList()));

		return responseDTO;
	}

	private UserActivityDTO convert(UserActivity userActivity) {
		return new ModelMapper().map(userActivity, UserActivityDTO.class);
	}
}