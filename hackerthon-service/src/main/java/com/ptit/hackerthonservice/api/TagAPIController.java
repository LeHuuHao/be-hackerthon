package com.ptit.hackerthonservice.api;

import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.dto.TagDTO;
import com.ptit.hackerthonservice.service.FileStoreService;
import com.ptit.hackerthonservice.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagAPIController {
	@Autowired
	private TagService tagService;

	@Autowired
	FileStoreService fileStoreService;

	final String PREFIX_FOLDER = "tag/";// thu muc chua tag

	@PostMapping("/")
	public ResponseDTO<TagDTO> create(@ModelAttribute @Valid TagDTO tagDTO) throws IOException {
		if (tagDTO.getFile() != null && !tagDTO.getFile().isEmpty()) {
			tagDTO.setFeatureImage(fileStoreService.writeFile(tagDTO.getFile(), PREFIX_FOLDER));
		}
		tagService.create(tagDTO);
		return ResponseDTO.<TagDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(tagDTO).build();
	}

	@PutMapping(value = "/")
	public ResponseDTO<Void> update(@ModelAttribute @Valid TagDTO tagDTO) throws IOException {
		if (tagDTO.getFile() != null && !tagDTO.getFile().isEmpty()) {
			tagDTO.setFeatureImage(fileStoreService.writeFile(tagDTO.getFile(), PREFIX_FOLDER));
		}
		tagService.update(tagDTO);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@GetMapping(value = "/{id}")
	public ResponseDTO<TagDTO> get(@PathVariable(value = "id") int id) {
		return ResponseDTO.<TagDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(tagService.get(id))
				.build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseDTO<Void> delete(@PathVariable(value = "id") int id) {
		tagService.delete(id);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@DeleteMapping("/all/{ids}")
	public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Integer> ids) {
		tagService.deleteAll(ids);
		return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
	}

	@PostMapping("/search")
	public ResponseDTO<List<TagDTO>> search(@RequestBody @Valid SearchDTO searchDTO) {
		return tagService.find(searchDTO);
	}

	@GetMapping("/slug/{slug}")
	public ResponseDTO<TagDTO> findBySlug(@PathVariable(value = "slug") String slug) {
		return ResponseDTO.<TagDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
				.data(tagService.finBySlug(slug)).build();
	}
}
