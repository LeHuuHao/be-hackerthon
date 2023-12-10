package com.ptit.hackerthonservice.service;


import com.ptit.hackerthonservice.dto.PrivilegeDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.entity.Privilege;
import com.ptit.hackerthonservice.repository.PrivilegeRepository;
import com.ptit.hackerthonservice.utils.CacheNames;
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

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface PrivilegeService {
	void create(PrivilegeDTO privilegeDTO);

	void update(PrivilegeDTO privilegeDTO);

	void delete(int id);

	void deleteAll(List<Integer> ids);

	PrivilegeDTO get(int id);

	ResponseDTO<List<PrivilegeDTO>> find(SearchDTO searchDTO);

	List<PrivilegeDTO> findAll();

}

@Service
class PrivilegeServiceImpl implements PrivilegeService {
	@Autowired
	PrivilegeRepository privilegeRepository;

	@Override
	@Transactional
	@CacheEvict(cacheNames = CacheNames.CACHE_PRIVILEGES, allEntries = true)
	public void create(PrivilegeDTO privilegeDTO) {
		ModelMapper mapper = new ModelMapper();
		Privilege privilege = mapper.map(privilegeDTO, Privilege.class);
		privilegeRepository.save(privilege);
		privilegeDTO.setId(privilege.getId());
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = CacheNames.CACHE_PRIVILEGES, allEntries = true)
	public void update(PrivilegeDTO privilegeDTO) {
		ModelMapper mapper = new ModelMapper();
		mapper.createTypeMap(PrivilegeDTO.class, Privilege.class).setProvider(
				p -> privilegeRepository.findById(privilegeDTO.getId()).orElseThrow(NoResultException::new));

		Privilege privilege = mapper.map(privilegeDTO, Privilege.class);
		privilegeRepository.save(privilege);
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = CacheNames.CACHE_PRIVILEGES, allEntries = true)
	public void delete(int id) {
		privilegeRepository.deleteById(id);
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = CacheNames.CACHE_PRIVILEGES, allEntries = true)
	public void deleteAll(List<Integer> ids) {
		privilegeRepository.deleteAllById(ids);
	}

	@Override
	public PrivilegeDTO get(int id) {
		return privilegeRepository.findById(id).map(privilege -> convert(privilege))
				.orElseThrow(NoResultException::new);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseDTO<List<PrivilegeDTO>> find(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		Page<Privilege> page = privilegeRepository.find(searchDTO.getValue(), pageable);

		ResponseDTO<List<PrivilegeDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(privilege -> convert(privilege)).collect(Collectors.toList()));
		return responseDTO;
	}

	@Override
	@Cacheable(CacheNames.CACHE_PRIVILEGES)
	public List<PrivilegeDTO> findAll() {
		List<Privilege> privileges = privilegeRepository.findAll();
		return privileges.stream().map(privilege -> convert(privilege)).collect(Collectors.toList());
	}

	private PrivilegeDTO convert(Privilege privilege) {
		return new ModelMapper().map(privilege, PrivilegeDTO.class);
	}
}
