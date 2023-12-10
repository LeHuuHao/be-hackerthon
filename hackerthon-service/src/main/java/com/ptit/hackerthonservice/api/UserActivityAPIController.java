package com.ptit.hackerthonservice.api;


import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.dto.UserActivityDTO;
import com.ptit.hackerthonservice.service.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user-activity")
public class UserActivityAPIController {
	@Autowired
	UserActivityService activityService;

	@PostMapping("/search")
	public ResponseDTO<List<UserActivityDTO>> search(@RequestBody @Valid SearchDTO searchDTO) {
		return activityService.find(searchDTO);
	}
}
