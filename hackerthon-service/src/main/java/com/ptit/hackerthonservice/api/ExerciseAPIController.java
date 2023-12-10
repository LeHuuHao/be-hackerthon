package com.ptit.hackerthonservice.api;


import com.ptit.hackerthonservice.dto.ExerciseDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.service.ExerciseService;
import com.ptit.hackerthonservice.utils.RoleEnum;
import com.ptit.hackerthonservice.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exercise")
public class ExerciseAPIController {
	@Autowired
	private ExerciseService exerciseService;

	@PostMapping("/")
	public ResponseDTO<ExerciseDTO> create(@RequestBody @Valid ExerciseDTO exerciseDTO) throws IOException {
		exerciseService.create(exerciseDTO);
		return ResponseDTO.<ExerciseDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(exerciseDTO).build();
	}

	@PutMapping(value = "/")
	public ResponseDTO<Void> update(@RequestBody @Valid ExerciseDTO exerciseDTO) throws IOException {
		exerciseService.update(exerciseDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@GetMapping(value = "/{id}")
	public ResponseDTO<ExerciseDTO> get(@PathVariable(value = "id") long id) {
		return ResponseDTO.<ExerciseDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(exerciseService.get(id)).build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") long id) {
		exerciseService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@DeleteMapping("/all/{ids}")
	public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Long> ids) {
		exerciseService.deleteAll(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PostMapping("/search")
	public ResponseDTO<List<ExerciseDTO>> search(@RequestBody @Valid SearchDTO searchDTO,
			Principal principal) {
		Set<String> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.map(i -> i.getAuthority()).collect(Collectors.toSet());

		if (authorities.contains(RoleEnum.EDITOR.getRoleName())) {
			searchDTO.getFilterBys().put("createdBy", principal.getName());
		}

		searchDTO.getFilterBys().put("authorities", String.join(",", authorities));

		if (authorities.contains(RoleEnum.ANONYMOUS.getRoleName()) || authorities.contains(RoleEnum.MEMBER.getRoleName())) {
			searchDTO.getFilterBys().put("status", StatusEnum.ACTIVE.name());
		}

		return exerciseService.find(searchDTO);
	}

	@GetMapping(value = "/slug/{slug}")
	public ResponseDTO<ExerciseDTO> findBySlug(@PathVariable(value = "slug") String slug) {
		return ResponseDTO.<ExerciseDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(exerciseService.finBySlug(slug)).build();
	}
	
}
