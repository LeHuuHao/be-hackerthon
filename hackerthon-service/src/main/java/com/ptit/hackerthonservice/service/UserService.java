package com.ptit.hackerthonservice.service;


import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import com.ptit.hackerthonservice.dto.*;
import com.ptit.hackerthonservice.entity.Role;
import com.ptit.hackerthonservice.entity.User;
import com.ptit.hackerthonservice.repository.RoleRepository;
import com.ptit.hackerthonservice.repository.UserRepository;
import com.ptit.hackerthonservice.utils.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


public interface UserService {
	void create(UserDTO userDTO);

	void resetPassword(UserDTO userDTO);

	void updateRole(UserDTO userDTO);

	void updateEmail(UserDTO userDTO);

	void updatePhone(UserDTO userDTO);

	void updateStatus(UserDTO userDTO);

	void updatePassword(UserDTO userDTO);

	void updatePhoto(UserDTO userDTO);

	void signup(UserDTO userDTO);

	void update(UserDTO userDTO);

	void delete(Integer id);

	void deleteAll(List<Integer> ids);

	UserDTO get(Integer id);

	ResponseDTO<List<UserDTO>> searchByTitle(SearchDTO searchDTO);

	UserDTO getUserByUsername(String username);

	CountLevelDTO getLevelWorkedByUser(Integer id);

	ResponseDTO<List<StatisticUserDTO>> statistic(SearchDTO searchDTO);

	List<StatisticUserDTO> statisticTop(SearchDTO searchDTO);
}

@Service
class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepo;

	@Autowired
	RoleRepository roleRepo;

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void create(UserDTO userDTO) {
		User user = new ModelMapper().map(userDTO, User.class);
		user.setPassword(PasswordGenerator.encode(userDTO.getPassword()));
		userRepo.save(user);
		userDTO.setId(user.getId());
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void signup(UserDTO userDTO) {
		User user = new ModelMapper().map(userDTO, User.class);
		user.setPassword(PasswordGenerator.encode(userDTO.getPassword()));
		user.setRole(roleRepo.findByName(RoleEnum.MEMBER.getRoleName()).orElse(null));
		user.setEnabled(true);

		userRepo.save(user);
		userDTO.setId(user.getId());
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void update(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);

		if (userDTO.getPhotoURL() != null) {
			user.setPhotoURL(userDTO.getPhotoURL());
		}
		user.setName(userDTO.getName());
		user.setAddress(userDTO.getAddress());
		user.setNote(userDTO.getNote());
		user.setBirthdate(userDTO.getBirthdate());
		user.setWebsite(userDTO.getWebsite());

		userRepo.save(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void updatePhoto(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);

		if (userDTO.getPhotoURL() != null) {
			user.setPhotoURL(userDTO.getPhotoURL());
		}

		userRepo.save(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void updatePhone(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);

		if (userDTO.getPhone() != null) {
			user.setPhone(userDTO.getPhone());
			userRepo.save(user);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void updateEmail(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);

		if (userDTO.getEmail() != null) {
			user.setEmail(userDTO.getEmail());
			userRepo.save(user);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void updateRole(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);
		Role role = roleRepo.findById(userDTO.getRole().getId()).orElseThrow(NoResultException::new);
		if (userDTO.getRole() != null) {
			user.setRole(role);
			userRepo.save(user);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void updateStatus(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);
		user.setEnabled(userDTO.getEnabled());
		userRepo.save(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void updatePassword(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);
		if (new BCryptPasswordEncoder().matches(userDTO.getOldPassword(), user.getPassword())) {
			user.setPassword(PasswordGenerator.encode(userDTO.getPassword()));
		} else throw new NoResultException("Sai mat khau hien tai");
		userRepo.save(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void resetPassword(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);
		user.setPassword(PasswordGenerator.encode(userDTO.getPassword()));
		userRepo.save(user);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void delete(Integer id) {
		userRepo.deleteById(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_USER_FIND, allEntries = true)
	public void deleteAll(List<Integer> ids) {
		userRepo.deleteAllById(ids);
	}

	@Override
	public UserDTO getUserByUsername(String username) {
		return userRepo.findByUsername(username).map(user -> convert(user)).orElseThrow(NoResultException::new);
	}

	@Override
	public UserDTO get(Integer id) {
		return userRepo.findById(id).map(user -> convert(user)).orElseThrow(NoResultException::new);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Cacheable(cacheNames = CacheNames.CACHE_USER_FIND, unless = "#result.totalElements == 0", key = "#searchDTO.toString()")
	public ResponseDTO<List<UserDTO>> searchByTitle(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		Page<User> page = userRepo.find(searchDTO.getValue(), pageable);

		ResponseDTO<List<UserDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(user -> convert(user)).collect(Collectors.toList()));
		return responseDTO;
	}
	@Override
	public ResponseDTO<List<StatisticUserDTO>> statistic(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		Date startAt = null;
		if (StringUtils.hasText(searchDTO.getFilterBys().get("startAt"))) {
			startAt = DateTimeUtils.parseDate(searchDTO.getFilterBys().get("startAt"), DateTimeUtils.DD_MM_YYYY);
		}

		Date endAt = null;
		if (StringUtils.hasText(searchDTO.getFilterBys().get("endAt"))) {
			endAt = DateTimeUtils.parseDate(searchDTO.getFilterBys().get("endAt"), DateTimeUtils.DD_MM_YYYY);
		}

		Page<StatisticUserDTO> page = userRepo.statistic(searchDTO.getValue(), StatusEnum.ACTIVE,startAt,endAt,pageable);

		ResponseDTO<List<StatisticUserDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().collect(Collectors.toList()));

		return responseDTO;
	}

	@Override
	public List<StatisticUserDTO> statisticTop(SearchDTO searchDTO) {
		Date startAt = null;
		Date endAt = null;

		if (StringUtils.hasText(searchDTO.getFilterBys().get("startAt"))) {
			startAt = DateTimeUtils.parseDate(searchDTO.getFilterBys().get("startAt"), DateTimeUtils.DD_MM_YYYY);
		}

		if (StringUtils.hasText(searchDTO.getFilterBys().get("endAt"))) {
			endAt = DateTimeUtils.parseDate(searchDTO.getFilterBys().get("endAt"), DateTimeUtils.DD_MM_YYYY);
		}

		return userRepo.statisticTop(startAt,endAt);
	}

	@Override
	public CountLevelDTO getLevelWorkedByUser(Integer id) {
		return userRepo.countLevel(id);
	}


	private UserDTO convert(User user) {
		return new ModelMapper().map(user, UserDTO.class);
	}

}