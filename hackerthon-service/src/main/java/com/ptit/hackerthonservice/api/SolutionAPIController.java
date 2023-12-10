package com.ptit.hackerthonservice.api;


import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.dto.SolutionDTO;
import com.ptit.hackerthonservice.service.SolutionService;
import com.ptit.hackerthonservice.utils.RoleEnum;
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
@RequestMapping("/solution")
public class SolutionAPIController {
	@Autowired
	private SolutionService solutionService;

	@PostMapping("/")
	public ResponseDTO<SolutionDTO> create(@RequestBody @Valid SolutionDTO solutionDTO) throws IOException {
		solutionService.create(solutionDTO);
		return ResponseDTO.<SolutionDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(solutionDTO).build();
	}

	@PutMapping(value = "/")
	public ResponseDTO<Void> update(@RequestBody @Valid SolutionDTO solutionDTO) throws IOException {
		solutionService.update(solutionDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@GetMapping(value = "/{id}")
	public ResponseDTO<SolutionDTO> get(@PathVariable(value = "id") long id) {
		return ResponseDTO.<SolutionDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(solutionService.get(id)).build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") long id) {
		solutionService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@DeleteMapping("/all/{ids}")
	public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Long> ids) {
		solutionService.deleteAll(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PostMapping("/search")
	public ResponseDTO<List<SolutionDTO>> search(@RequestBody @Valid SearchDTO searchDTO, Principal principal) {
		Set<String> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.map(i -> i.getAuthority()).collect(Collectors.toSet());

		if (authorities.contains(RoleEnum.EDITOR.getRoleName())) {
			searchDTO.getFilterBys().put("createdById", principal.getName());
		}

		return solutionService.find(searchDTO);
	}

	@GetMapping(value = "/slug/{slug}")
	public ResponseDTO<SolutionDTO> findBySlug(@PathVariable(value = "slug") String slug) {
		return ResponseDTO.<SolutionDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(solutionService.finBySlug(slug)).build();
	}

}
