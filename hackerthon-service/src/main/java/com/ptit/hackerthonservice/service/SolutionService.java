package com.ptit.hackerthonservice.service;


import com.github.slugify.Slugify;
import com.ptit.hackerthonservice.dto.ExerciseDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.dto.SolutionDTO;
import com.ptit.hackerthonservice.entity.Exercise;
import com.ptit.hackerthonservice.entity.ExerciseSolution;
import com.ptit.hackerthonservice.entity.Solution;
import com.ptit.hackerthonservice.entity.Statistic;
import com.ptit.hackerthonservice.repository.ExerciseRepo;
import com.ptit.hackerthonservice.repository.SolutionRepository;
import com.ptit.hackerthonservice.utils.CacheNames;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
import java.util.*;
import java.util.stream.Collectors;

public interface SolutionService {
	void create(SolutionDTO solutionDTO);

	void update(SolutionDTO solutionDTO);

	void delete(Long id);

	void deleteAll(List<Long> ids);

	SolutionDTO get(Long id);

	SolutionDTO finBySlug(String slug);

	ResponseDTO<List<SolutionDTO>> find(SearchDTO searchDTO);


}

@Service
class SolutionServiceImpl implements SolutionService {

	@Autowired
	SolutionRepository solutionRepository;

	@Autowired
	ExerciseRepo exerciseRepo;

	@Transactional
	@Override
	public void create(SolutionDTO solutionDTO) {
		final Slugify slugify = Slugify.builder().locale(Locale.forLanguageTag("vi")).build();
		String slug = slugify.slugify(solutionDTO.getTitle());
		while (true) {
			Solution solution = solutionRepository.findBySlug(slug).orElse(null);
			if (solution != null)
				slug = slug + "-" + System.currentTimeMillis();
			else
				break;
		}
		solutionDTO.setSlug(slug);

		// copy
		Solution solution = new ModelMapper().createTypeMap(SolutionDTO.class, Solution.class)
				.addMappings(mapping -> {
					mapping.skip(Solution :: setExerciseSolution);
				}).map(solutionDTO);

		if (solutionDTO.getExercise() != null) {
			ExerciseSolution exerciseSolution = new ExerciseSolution();
			exerciseSolution.setSolution(solution);
			exerciseSolution.setExercise(exerciseRepo.findById(solutionDTO.getExercise().getId()).orElseThrow());
			solution.setExerciseSolution(exerciseSolution);
		} else throw new NoSuchElementException();

		solution.setStatistic(new Statistic());
		solutionRepository.save(solution);
		solutionDTO.setId(solution.getId());
	}

	@Transactional
	@Override
	public void update(SolutionDTO solutionDTO) {
		final Solution solution = solutionRepository.findById(solutionDTO.getId())
				.orElseThrow(NoResultException :: new);

		// gen slug for solution
		if (! solution.getTitle().equals(solutionDTO.getTitle())) {
			final Slugify slugify = Slugify.builder().locale(Locale.forLanguageTag("vi")).build();
			String slug = slugify.slugify(solutionDTO.getTitle());
			while (true) {
				Solution s = solutionRepository.findBySlug(slug).orElse(null);
				if (s != null)
					slug = slug + "-" + System.currentTimeMillis();
				else
					break;
			}
			solutionDTO.setSlug(slug);
		} else
			solutionDTO.setSlug(solution.getSlug());

		// copy
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Solution saveSolution = mapper.createTypeMap(SolutionDTO.class, Solution.class)
				.setProvider(p -> solution).map(solutionDTO);

		solutionRepository.save(saveSolution);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		Solution solution = solutionRepository.findById(id).orElseThrow(NoResultException::new);
		if (solution != null) {
			solutionRepository.deleteById(id);
		}
	}

	@Transactional
	@Override
	public void deleteAll(List<Long> ids) {
		solutionRepository.deleteAllByIdInBatch(ids);
	}

	@Override
	@Cacheable(CacheNames.CACHE_SOLUTION)
	public SolutionDTO get(Long id) {
		return solutionRepository.findById(id).map(solution -> convert(solution)).orElseThrow(NoResultException::new);
	}

	@Override
	@Cacheable(cacheNames = CacheNames.CACHE_SOLUTION_FIND, unless = "#result.totalElements == 0", key = "#searchDTO.toString()")
	public ResponseDTO<List<SolutionDTO>> find(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections :: emptyList)
				.stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		Long exerciseId = null;
		if (StringUtils.hasText(searchDTO.getFilterBys().get("exerciseId"))) {
			exerciseId = Long.valueOf(searchDTO.getFilterBys().get("exerciseId"));
		}


		Integer createdById = null;
		if (StringUtils.hasText(searchDTO.getFilterBys().get("createdById"))) {
			createdById = Integer.valueOf(searchDTO.getFilterBys().get("createdById"));
		}

		List<Integer> tagIds = null;
		if (StringUtils.hasText(searchDTO.getFilterBys().get("tagIds"))) {
			String[] tagArr = searchDTO.getFilterBys().get("tagIds").split(",");
			tagIds = Arrays.stream(tagArr).map(Integer :: valueOf).collect(Collectors.toList());
		}


		Page<Solution> page = solutionRepository.findExerciseSolutions(searchDTO.getValue(), exerciseId, createdById, tagIds,
				pageable);;

		@SuppressWarnings("unchecked")
		ResponseDTO<List<SolutionDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(this :: convert).collect(Collectors.toList()));
		return responseDTO;
	}
	@Override
	public SolutionDTO finBySlug(String slug) {
		return solutionRepository.findBySlug(slug).map(this :: convert).orElseThrow();
	}


	private SolutionDTO convert(Solution solution) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		SolutionDTO solutionDTO = mapper.map(solution, SolutionDTO.class);

		if (solution.getExerciseSolution() != null) {
			Exercise exercise = solution.getExerciseSolution().getExercise();
			solutionDTO.setExercise(ExerciseDTO.builder().id(exercise.getId())
					.title(exercise.getTitle())
					.slug(exercise.getSlug())
					.build());
		}

		return solutionDTO;
	}


}
