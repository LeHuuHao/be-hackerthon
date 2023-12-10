package com.ptit.hackerthonservice.api;


import com.ptit.hackerthonservice.dto.BookmarkDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
@RestController
@RequestMapping("/bookmark")
public class BookmarkAPIController {
    @Autowired
    BookmarkService bookmarkService;

    @PostMapping("/")
    public ResponseDTO<BookmarkDTO> create(@RequestBody @Valid BookmarkDTO bookMarkDTO) throws IOException {
        bookmarkService.create(bookMarkDTO);
        return ResponseDTO.<BookmarkDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(bookMarkDTO).build();
    }

    @PutMapping(value = "/")
    public ResponseDTO<Void> update(@RequestBody @Valid BookmarkDTO bookMarkDTO) throws IOException {
        bookmarkService.update(bookMarkDTO);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @GetMapping(value = "/{exerciseId}")
    public ResponseDTO<BookmarkDTO> get(@PathVariable(value = "exerciseId") Long id, Principal principal) {
        return ResponseDTO.<BookmarkDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(bookmarkService.get(Integer.valueOf(principal.getName()), id))
                .build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseDTO<Void> delete(@PathVariable(value = "id") Long id) {
        bookmarkService.delete(id);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @DeleteMapping("/all/{ids}")
    public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Long> ids) {
        bookmarkService.deleteAll(ids);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @PostMapping("/search")
    public ResponseDTO<List<BookmarkDTO>> search(@RequestBody @Valid SearchDTO searchDTO, Principal principal) {
        searchDTO.getFilterBys().put("uid", principal.getName());
        return bookmarkService.find(searchDTO);
    }


}
