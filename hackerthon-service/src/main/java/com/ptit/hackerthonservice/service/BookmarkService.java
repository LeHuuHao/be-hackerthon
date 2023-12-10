package com.ptit.hackerthonservice.service;

import com.ptit.hackerthonservice.dto.BookmarkDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.entity.Bookmark;
import com.ptit.hackerthonservice.entity.Exercise;
import com.ptit.hackerthonservice.repository.BookmarkRepository;
import com.ptit.hackerthonservice.repository.ExerciseRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

public interface BookmarkService {

	void create(BookmarkDTO bookMarkDTO);

	void update(BookmarkDTO bookMarkDTO);

	void delete(Long id);

	void deleteAll(List<Long> ids);

	BookmarkDTO get(Integer userId, Long exerciseID);

	ResponseDTO<List<BookmarkDTO>> find(SearchDTO searchDTO);
}

@Service
class BookmarkServiceImpl implements BookmarkService {

	@Autowired
	BookmarkRepository bookmarkRepository;

	@Autowired
	ExerciseRepo exerciseRepo;

	@Transactional
	@Override
	public void create(BookmarkDTO bookMarkDTO) {
		ModelMapper mapper = new ModelMapper();
		Bookmark bookmark = mapper.map(bookMarkDTO, Bookmark.class);
		Exercise exercise = exerciseRepo.findById(bookMarkDTO.getExercise().getId()).orElseThrow();
		bookmark.setExercise(exercise);
		bookmarkRepository.save(bookmark);
		bookMarkDTO.setId(bookmark.getId());
	}

	@Transactional
	@Override
	public void update(BookmarkDTO bookMarkDTO) {
		Bookmark bookmark = new ModelMapper().map(bookMarkDTO, Bookmark.class);
		bookmarkRepository.save(bookmark);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		Bookmark bookmark = bookmarkRepository.findById(id).orElseThrow(NoResultException::new);
		if (bookmark != null) {
			bookmarkRepository.deleteById(id);
		}
	}

	@Transactional
	@Override
	public void deleteAll(List<Long> ids) {
		bookmarkRepository.deleteAllById(ids);
	}

	@Override
	public BookmarkDTO get(Integer userId, Long exerciseId) {
		Bookmark bookmark = bookmarkRepository.getBookMarkByUserId(userId, exerciseId);

		if (bookmark == null||userId==null) {
			return null;
		}
		BookmarkDTO bookMarkDTO = new ModelMapper().map(bookmark, BookmarkDTO.class);
		return bookMarkDTO;
	}

	@Override
	public ResponseDTO<List<BookmarkDTO>> find(SearchDTO searchDTO) {
		List<Sort.Order> orders = Optional.ofNullable(searchDTO.getOrders()).orElseGet(Collections::emptyList).stream()
				.map(order -> {
					if (order.getOrder().equals(SearchDTO.ASC))
						return Sort.Order.asc(order.getProperty());

					return Sort.Order.desc(order.getProperty());
				}).collect(Collectors.toList());

		Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), Sort.by(orders));

		// filter by
		Integer uid = null;
		if (StringUtils.hasText(searchDTO.getFilterBys().get("uid"))) {
			uid = Integer.valueOf(searchDTO.getFilterBys().get("uid"));
		}

		Page<Bookmark> page = bookmarkRepository.find(searchDTO.getValue(), uid, pageable);

		@SuppressWarnings("unchecked")
		ResponseDTO<List<BookmarkDTO>> responseDTO = new ModelMapper().map(page, ResponseDTO.class);
		responseDTO.setData(page.get().map(bookMark -> convert(bookMark)).collect(Collectors.toList()));
		return responseDTO;
	}

	private BookmarkDTO convert(Bookmark bookmark) {
		return new ModelMapper().map(bookmark, BookmarkDTO.class);
	}
}