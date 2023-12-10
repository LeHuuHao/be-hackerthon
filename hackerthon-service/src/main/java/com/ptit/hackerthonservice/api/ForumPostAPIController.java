package com.ptit.hackerthonservice.api;


import com.ptit.hackerthonservice.dto.ForumPostDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.service.ForumPostService;
import com.ptit.hackerthonservice.utils.RoleEnum;
import com.ptit.hackerthonservice.utils.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/forum-post")
public class ForumPostAPIController {
    @Autowired
    private ForumPostService forumPostService;

    @PostMapping("/")
    public ResponseDTO<ForumPostDTO> create(@RequestBody ForumPostDTO forumPostDTO) {
        forumPostService.create(forumPostDTO);
        return ResponseDTO.<ForumPostDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(forumPostDTO).build();
    }

    @PutMapping("/")
    public ResponseDTO<Void> update(@RequestBody @Valid ForumPostDTO postDTO) {
        forumPostService.update(postDTO);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @GetMapping("/{id}")
    public ResponseDTO<ForumPostDTO> get(@PathVariable(value = "id") long id) {
        return ResponseDTO.<ForumPostDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(forumPostService.get(id)).build();
    }

    @GetMapping("/slug/{slug}")
    public ResponseDTO<ForumPostDTO> get(@PathVariable(name = "slug") String slug) {
        return ResponseDTO.<ForumPostDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(forumPostService.finBySlug(slug)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseDTO<Void> delete(@PathVariable(value = "id") long id) {
        forumPostService.delete(id);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @DeleteMapping("/all/{ids}")
    public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Long> ids) {
        forumPostService.deleteAll(ids);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @PostMapping("/search")
    public ResponseDTO<List<ForumPostDTO>> search(@RequestBody @Valid SearchDTO searchDTO, Authentication authentication) {
        List<String> authorities = authentication.getAuthorities().stream().map(g -> g.getAuthority()).collect(Collectors.toList());

        if (authorities.contains(RoleEnum.EDITOR.getRoleName())) {
            searchDTO.getFilterBys().put("uid", authentication.getName());
        }
        return forumPostService.find(searchDTO);
    }

    @PostMapping("/search/active")
    public ResponseDTO<List<ForumPostDTO>> searchActive(@RequestBody @Valid SearchDTO searchDTO) {
        searchDTO.getFilterBys().put("status", StatusEnum.ACTIVE.name());
        return forumPostService.find(searchDTO);
    }
}
