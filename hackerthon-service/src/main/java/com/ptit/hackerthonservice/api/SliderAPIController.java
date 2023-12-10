package com.ptit.hackerthonservice.api;


import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.dto.SliderDTO;
import com.ptit.hackerthonservice.service.FileStoreService;
import com.ptit.hackerthonservice.service.SliderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/slider")
public class SliderAPIController {
	@Autowired
	private SliderService sliderService;

	@Autowired
	FileStoreService fileStoreService;

	final String PREFIX_FOLDER = "slider/";

	@PostMapping("/")
	public ResponseDTO<SliderDTO> create(@ModelAttribute @Valid SliderDTO sliderDTO) throws IOException {
		if (sliderDTO.getFile() != null && !sliderDTO.getFile().isEmpty()) {
			sliderDTO.setFeatureImage(fileStoreService.writeFile(sliderDTO.getFile(), PREFIX_FOLDER));
		}
		sliderService.create(sliderDTO);
		return ResponseDTO.<SliderDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(sliderDTO).build();
	}

	@PutMapping("/")
	public ResponseDTO<Void> update(@ModelAttribute SliderDTO sliderDTO) throws IOException {
		if (sliderDTO.getFile() != null && !sliderDTO.getFile().isEmpty()) {
			sliderDTO.setFeatureImage(fileStoreService.writeFile(sliderDTO.getFile(), PREFIX_FOLDER));
		}
		sliderService.update(sliderDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@GetMapping("/{id}")
	public ResponseDTO<SliderDTO> get(@PathVariable(value = "id") int id) {
		return ResponseDTO.<SliderDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(sliderService.get(id))
				.build();
	}

	@DeleteMapping("/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") int id) {
		sliderService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@DeleteMapping("/all/{ids}")
	public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Integer> ids) {
		sliderService.deleteAll(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PostMapping("/search")
	public ResponseDTO<List<SliderDTO>> search(@RequestBody @Valid SearchDTO searchDTO) {
		return sliderService.find(searchDTO);
	}
}
