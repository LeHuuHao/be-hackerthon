package com.ptit.hackerthonservice.service;


import com.ptit.hackerthonservice.dto.ProfileDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.entity.Profile;
import com.ptit.hackerthonservice.repository.ProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ProfileService {

	void update(ProfileDTO profileDTO);

	void delete(String id);

	void deleteAll(List<String> ids);

	ProfileDTO get(String id);

	ResponseDTO<List<ProfileDTO>> find(SearchDTO searchDTO);
}

@Service
class ScoreServiceImpl implements ProfileService {
	@Autowired
	ProfileRepository profileRepository;

	@Override
	@Transactional
	public void update(ProfileDTO profileDTO) {
		Profile profile = new ModelMapper().map(profileDTO, Profile.class);
		profileRepository.save(profile);
	}

	@Override
	@Transactional
	public void delete(String id) {
		profileRepository.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll(List<String> ids) {
		profileRepository.deleteAllById(ids);
	}

	@Override
	public ProfileDTO get(String id) {
		Profile profile = profileRepository.findById(id).orElseThrow(NoResultException::new);
		return new ModelMapper().map(profile, ProfileDTO.class);
	}

	@Override
	public ResponseDTO<List<ProfileDTO>> find(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		Page<Profile> page = profileRepository.findAll(pageable);

		@SuppressWarnings("unchecked")
		ResponseDTO<List<ProfileDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(profile -> convert(profile)).collect(Collectors.toList()));
		return responseDTO;
	}

	private ProfileDTO convert(Profile profile) {
		return new ModelMapper().map(profile, ProfileDTO.class);
	}
}
