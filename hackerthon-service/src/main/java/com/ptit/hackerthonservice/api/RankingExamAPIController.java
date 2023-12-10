package com.ptit.hackerthonservice.api;

import com.ptit.hackerthonservice.dto.RankingExamDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.service.RankingExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ranking-exam")
public class RankingExamAPIController {
    @Autowired
    private RankingExamService rankingExamService;

    @PostMapping("/")
    public ResponseDTO<RankingExamDTO> create(@RequestBody @Valid RankingExamDTO rankingExamDTO) throws IOException {
        rankingExamService.create(rankingExamDTO);
        return ResponseDTO.<RankingExamDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(rankingExamDTO).build();
    }

    @PutMapping(value = "/")
    public ResponseDTO<Void> update(@RequestBody @Valid RankingExamDTO rankingExamDTO) throws IOException {
        rankingExamService.update(rankingExamDTO);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseDTO<RankingExamDTO> get(@PathVariable(value = "id") Long id) {
        return ResponseDTO.<RankingExamDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(rankingExamService.get(id)).build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseDTO<Void> delete(@PathVariable(value = "id") Long id) {
        rankingExamService.delete(id);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @DeleteMapping("/delete/all/{ids}")
    public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Long> ids) {
        rankingExamService.deleteAll(ids);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }


    @PostMapping("/search")
    public ResponseDTO<List<RankingExamDTO>> search(@RequestBody @Valid SearchDTO searchDTO) {
        return rankingExamService.find(searchDTO);
    }

}
