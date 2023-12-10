package com.ptit.hackerthonservice.api;


import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.dto.SolutionCommentDTO;
import com.ptit.hackerthonservice.service.SolutionCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@RestController
@RequestMapping("/solution-comment")
public class SolutionCommentAPIController {
    @Autowired
    private SolutionCommentService solutionCommentService;

    @PostMapping("/")
    public ResponseDTO<SolutionCommentDTO> create(@RequestBody @Valid SolutionCommentDTO solutionCommentDTO) {
        solutionCommentService.create(solutionCommentDTO);
        return ResponseDTO.<SolutionCommentDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(solutionCommentDTO).build();
    }

    @PutMapping(value = "/")
    public ResponseDTO<Void> update(@RequestBody @Valid SolutionCommentDTO solutionCommentDTO) {
        solutionCommentService.update(solutionCommentDTO);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseDTO<SolutionCommentDTO> get(@PathVariable(value = "id") Long id) {
        return ResponseDTO.<SolutionCommentDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(solutionCommentService.get(id))
                .build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseDTO<Void> delete(@PathVariable(value = "id") Long id) {
        solutionCommentService.delete(id);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @DeleteMapping("/all/{ids}")
    public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Long> ids) {
        solutionCommentService.deleteAll(ids);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @PostMapping("/search")
    public ResponseDTO<List<SolutionCommentDTO>> search(@RequestBody @Valid SearchDTO searchDTO) {
        return solutionCommentService.find(searchDTO);
    }
}
