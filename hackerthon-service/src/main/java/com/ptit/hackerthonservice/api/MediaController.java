package com.ptit.hackerthonservice.api;


import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.service.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/media")
public class MediaController {

	@Autowired
	FileStoreService fileStoreService;

	@PostMapping("/upload")
	public ResponseDTO<String> create(@RequestParam("file") MultipartFile file,
									  @RequestParam(name = "prefix", required = false, defaultValue = "") String prefix,
									  Principal principal) throws IOException {
		String filepath = fileStoreService.writeFile(file, prefix);
		return ResponseDTO.<String>builder().code(String.valueOf(HttpStatus.OK.value())).data(filepath).build();
	}

	@PostMapping("/uploads")
	public ResponseDTO<List<String>> create(@RequestParam("files") List<MultipartFile> files,
			@RequestParam(name = "prefix", required = false, defaultValue = "") String prefix,
			Principal principal) throws IOException {
		List<String> filepaths = fileStoreService.writeFiles(files, prefix);
		return ResponseDTO.<List<String>>builder().code(String.valueOf(HttpStatus.OK.value())).data(filepaths).build();
	}

	@GetMapping("/download/**")
	public ResponseEntity<Resource> download(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String filepath = uri.replace("/media/download", "");

		return ResponseEntity.ok().cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
				.body(fileStoreService.readFile(filepath));
	}

}