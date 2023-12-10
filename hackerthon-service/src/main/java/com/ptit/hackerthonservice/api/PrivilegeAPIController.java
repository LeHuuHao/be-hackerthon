package com.ptit.hackerthonservice.api;


import com.ptit.hackerthonservice.dto.PrivilegeDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/privilege")
public class PrivilegeAPIController {
	@Autowired
	private PrivilegeService privilegeService;

	@PostMapping("/")
	public ResponseDTO<PrivilegeDTO> create(@RequestBody @Valid PrivilegeDTO privilegeDTO) {
		privilegeService.create(privilegeDTO);
		return ResponseDTO.<PrivilegeDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(privilegeDTO)
				.build();
	}

	@PutMapping("/")
	public ResponseDTO<Void> update(@RequestBody @Valid PrivilegeDTO privilegeDTO) {
		privilegeService.update(privilegeDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@GetMapping("/{id}")
	public ResponseDTO<PrivilegeDTO> get(@PathVariable(value = "id") int id) {
		return ResponseDTO.<PrivilegeDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(privilegeService.get(id)).build();
	}

	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") int id) {
		privilegeService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@DeleteMapping("/all/{ids}")
	public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Integer> ids) {
		privilegeService.deleteAll(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PostMapping("/search")
	public ResponseDTO<List<PrivilegeDTO>> search(@RequestBody @Valid SearchDTO searchDTO) {
		return privilegeService.find(searchDTO);
	}
}
