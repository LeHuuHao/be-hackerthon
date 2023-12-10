package com.ptit.hackerthonservice.api;


import com.ptit.hackerthonservice.dto.CommentDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentAPIController {
    @Autowired
    CommentService commentService;

    @PostMapping("/")
    public ResponseDTO<CommentDTO> create(@RequestBody @Valid CommentDTO commentDTO) throws IOException {
        commentService.create(commentDTO);
        return ResponseDTO.<CommentDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(commentDTO).build();
    }

    @PutMapping(value = "/")
    public ResponseDTO<Void> update(@RequestBody @Valid CommentDTO commentDTO) throws IOException {
        commentService.update(commentDTO);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @PutMapping("/{id}/like")
    public ResponseDTO<Void> updateLike(@PathVariable(value = "id") long id) {
        commentService.updateLike(id);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseDTO<Void> delete(@PathVariable(value = "id") long id) {
        commentService.delete(id);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @DeleteMapping("/all/{ids}")
    public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Long> ids) {
        commentService.deleteAll(ids);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @PostMapping("/search")
    public ResponseDTO<List<CommentDTO>> search(@RequestBody @Valid SearchDTO searchDTO) {
        return commentService.find(searchDTO);
    }

}
