package com.ptit.hackerthonservice.api;

import com.ptit.hackerthonservice.dto.AnswerDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.RunCodeDTO;
import com.ptit.hackerthonservice.service.CheckCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/code")
public class CheckCodeAPIController {

	@Autowired
	CheckCodeService checkCodeService;

	@PostMapping("/check")
	public ResponseDTO<AnswerDTO> checkCode(@RequestBody RunCodeDTO runCodeDTO) throws IOException {
		return ResponseDTO.<AnswerDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(checkCodeService.checkCode(runCodeDTO)).build();
	}

	@PostMapping("/run")
	public ResponseDTO<AnswerDTO> runCode(@RequestBody RunCodeDTO runCodeDTO) throws IOException {
		return ResponseDTO.<AnswerDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(checkCodeService.runCode(runCodeDTO)).build();
	}

	@PostMapping("/submit")
	public ResponseDTO<AnswerDTO> submitCode(@RequestBody RunCodeDTO runCodeDTO) throws IOException {
		return ResponseDTO.<AnswerDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(checkCodeService.submitCode(runCodeDTO)).build();
	}

	@PostMapping("/submit/exam")
	public ResponseDTO<AnswerDTO> submitCodeExam(@RequestBody RunCodeDTO runCodeDTO, Principal principal) throws IOException {
		return ResponseDTO.<AnswerDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(checkCodeService.submitExam(runCodeDTO,principal)).build();
	}
}
