package com.ptit.hackerthonservice.service;

import com.ptit.hackerthonservice.dto.AnswerDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.entity.Answer;
import com.ptit.hackerthonservice.repository.AnswerRepo;
import com.ptit.hackerthonservice.repository.ExerciseRepo;
import com.ptit.hackerthonservice.utils.CacheNames;
import com.ptit.hackerthonservice.utils.RoleEnum;
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
import java.util.Set;
import java.util.stream.Collectors;

public interface AnswerService {
	void create(AnswerDTO answerDTO);

	void update(AnswerDTO answerDTO);

	void delete(Long id);

	void deleteAll(List<Long> ids);

	AnswerDTO check(Integer id, Long exerciseId);

	Long getAnswerByLastWeek(Integer uid);

	ResponseDTO<List<AnswerDTO>> find(SearchDTO searchDTO);
}

@Service
class StudentServiceImpl implements AnswerService {

	@Autowired
	AnswerRepo anwserRepo;

	@Autowired
	ExerciseRepo exerciseRepo;

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_ANSWER_FIND, allEntries = true)
	public void create(AnswerDTO answerDTO) {
		Answer answer = new ModelMapper().createTypeMap(AnswerDTO.class, Answer.class).setPostConverter(context -> {
			context.getDestination()
					.setExercise(exerciseRepo.findById(context.getSource().getExercise().getId()).orElse(null));
			return context.getDestination();
		}).map(answerDTO);
		
		answer.getTestCasesResults().forEach(testcase -> testcase.setAnswer(answer));

		anwserRepo.save(answer);
		
		answerDTO.setId(answer.getId());
	}

	@Override
	@Transactional
	public void update(AnswerDTO answerDTO) {
		ModelMapper mapper = new ModelMapper();
		mapper.createTypeMap(AnswerDTO.class, Answer.class)
				.setProvider(p -> anwserRepo.findById(answerDTO.getId()).orElseThrow(NoResultException::new));

		Answer answer = mapper.map(answerDTO, Answer.class);
		anwserRepo.save(answer);
	}

	@Override
	public AnswerDTO check(Integer uid,Long exerciseId) {
		Answer answer = anwserRepo.checkUser(uid,exerciseId).orElseGet(() -> {
			return null;
		});
		if(answer==null){
			return  null;
		}
		return new ModelMapper().map(answer,AnswerDTO.class);
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_ANSWER_FIND, allEntries = true)
	public void delete(Long id) {
		Answer answer = anwserRepo.findById(id).orElseThrow(NoResultException::new);
		if (answer != null) {
			anwserRepo.deleteById(id);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = CacheNames.CACHE_ANSWER_FIND, allEntries = true)
	public void deleteAll(List<Long> ids) {
		anwserRepo.deleteAllById(ids);
	}



	@Override
	@Transactional
	public Long getAnswerByLastWeek(Integer uid) {
		return anwserRepo.findAnswerByWeek(uid);
	}

	@Override
	@Cacheable(cacheNames = CacheNames.CACHE_ANSWER_FIND, unless = "#result.totalElements == 0", key = "#searchDTO.toString()")
	public ResponseDTO<List<AnswerDTO>> find(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		// filter by
		Long exerciseId = null;
		if (StringUtils.hasText(searchDTO.getFilterBys().get("exerciseId"))) {
			exerciseId = Long.valueOf(searchDTO.getFilterBys().get("exerciseId"));
		}

		Integer uid = null;
		if (StringUtils.hasText(searchDTO.getFilterBys().get("createdById"))) {
			uid = Integer.valueOf(searchDTO.getFilterBys().get("createdById"));
		}
		final Set<String> authorities = Set.of(searchDTO.getFilterBys().get("authorities").split(","));

		Page<Answer> page = anwserRepo.find(searchDTO.getValue(),exerciseId, uid, pageable);

		@SuppressWarnings("unchecked")
		ResponseDTO<List<AnswerDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(answer -> convert(answer,authorities)).collect(Collectors.toList()));
		return responseDTO;
	}

	private AnswerDTO convert(Answer answer, Set<String> authorities) {
		AnswerDTO answerDTO = new ModelMapper().map(answer, AnswerDTO.class);
		if (authorities.contains(RoleEnum.MANAGER.getRoleName())||authorities.contains(RoleEnum.ADMIN.getRoleName()))
			return answerDTO;

		// hide test case
		answerDTO.getTestCasesResults().forEach(testCaseDTO -> {
			testCaseDTO.setInput("...");
			testCaseDTO.setOutput("...");
			testCaseDTO.setExpectedOutput("...");
		});

		if (authorities.contains(RoleEnum.ANONYMOUS.getRoleName())) {
			answerDTO.setSourceCode("...");
		}

		return answerDTO;
	}
}
