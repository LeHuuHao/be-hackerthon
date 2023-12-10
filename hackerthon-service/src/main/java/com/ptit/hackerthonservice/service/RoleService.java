package com.ptit.hackerthonservice.service;


import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.RoleDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.entity.Role;
import com.ptit.hackerthonservice.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
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

public interface RoleService {
	void create(RoleDTO roleDTO);

	void update(RoleDTO roleDTO);

	void delete(int id);

	void deleteAll(List<Integer> ids);

	RoleDTO get(int id);

	ResponseDTO<List<RoleDTO>> find(SearchDTO searchDTO);

}

@Service
class RoleServiceImpl implements RoleService {
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	CacheManager cacheManager;

	@Override
	@Transactional
	public void create(RoleDTO roleDTO) {
		ModelMapper mapper = new ModelMapper();
		Role role = mapper.map(roleDTO, Role.class);
		roleRepository.save(role);
		roleDTO.setId(role.getId());
	}

	@Override
	@Transactional
	public void update(RoleDTO roleDTO) {
		ModelMapper mapper = new ModelMapper();
		mapper.createTypeMap(RoleDTO.class, Role.class).addMappings(mp -> {
			mp.skip(Role::setId);
		}).setProvider(p -> roleRepository.findById(roleDTO.getId()).orElseThrow(NoResultException::new));

		Role role = mapper.map(roleDTO, Role.class);
		roleRepository.save(role);
	}

	@Override
	@Transactional
	public void delete(int id) {
		roleRepository.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll(List<Integer> ids) {
		roleRepository.deleteAllById(ids);
	}

	@Override
	public RoleDTO get(int id) {
		return roleRepository.findById(id).map(role -> convert(role)).orElseThrow(NoResultException::new);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseDTO<List<RoleDTO>> find(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		Page<Role> page = roleRepository.find(searchDTO.getValue(), pageable);

		ResponseDTO<List<RoleDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(role -> convert(role)).collect(Collectors.toList()));
		return responseDTO;
	}

	private RoleDTO convert(Role role) {
		return new ModelMapper().map(role, RoleDTO.class);
	}
}
