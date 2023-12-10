package com.ptit.hackerthonservice.api;


import com.ptit.hackerthonservice.dto.*;
import com.ptit.hackerthonservice.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/exam")
public class ExamAPIController {
	@Autowired
	private ExamService examService;

	@PostMapping("/")
	public ResponseDTO<ExamDTO> create(@RequestBody @Valid ExamDTO examDTO) {
		examService.create(examDTO);
		return ResponseDTO.<ExamDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(examDTO).build();
	}

	@PutMapping(value = "/")
	public ResponseDTO<Void> update(@RequestBody @Valid ExamDTO examDTO) {
		examService.update(examDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PutMapping(value = "/end")
	public ResponseDTO<Void> updateEnd(@RequestBody @Valid ExamDTO examDTO) {
		examService.updateEnd(examDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}


	@GetMapping(value = "/{id}")
	public ResponseDTO<ExamDTO> get(@PathVariable(value = "id") Long id) {
		return ResponseDTO.<ExamDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(examService.get(id))
				.build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") Long id) {
		examService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@DeleteMapping("/delete/all/{ids}")
	public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Long> ids) {
		examService.deleteAll(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PostMapping("/search")
	public ResponseDTO<List<ExamDTO>> search(@RequestBody @Valid SearchDTO searchDTO) {
		return examService.find(searchDTO);
	}

	@GetMapping(value = "/slug/{slug}")
	public ResponseDTO<ExamDTO> findBySlug(@PathVariable(value = "slug") String slug) {
		return ResponseDTO.<ExamDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(examService.finBySlug(slug)).build();
	}


}
