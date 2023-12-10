package com.ptit.hackerthonservice.api;


import com.ptit.hackerthonservice.dto.AnswerDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.service.AnswerService;
import com.ptit.hackerthonservice.utils.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/answer")
public class AnswerAPIController {
	@Autowired
	AnswerService answerService;

	@PostMapping("/")
	public ResponseDTO<AnswerDTO> create(@RequestBody @Valid AnswerDTO answerDTO) throws IOException {
		answerService.create(answerDTO);
		return ResponseDTO.<AnswerDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(answerDTO).build();
	}

	@PutMapping(value = "/")
	public ResponseDTO<Void> update(@RequestBody @Valid AnswerDTO answerDTO) throws IOException {
		answerService.update(answerDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}


	@DeleteMapping(value = "/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") Long id) {
		answerService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@DeleteMapping("/all/{ids}")
	public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Long> ids) {
		answerService.deleteAll(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PostMapping("/search")
	public ResponseDTO<List<AnswerDTO>> search(@RequestBody @Valid SearchDTO searchDTO, Principal principal) {
		Set<String> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.map(GrantedAuthority:: getAuthority).collect(Collectors.toSet());

		if (!authorities.contains(RoleEnum.MANAGER.getRoleName())) {
			searchDTO.getFilterBys().put("createdById", principal.getName());
		}

		searchDTO.getFilterBys().put("authorities", String.join(",", authorities));

		return answerService.find(searchDTO);
	}

	@GetMapping("/last-week/{id}")
	public ResponseDTO<Long> getLastWeek(@PathVariable(value = "id") Integer id) {
		return ResponseDTO.<Long>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(answerService.getAnswerByLastWeek(id)).build();
	}
}
