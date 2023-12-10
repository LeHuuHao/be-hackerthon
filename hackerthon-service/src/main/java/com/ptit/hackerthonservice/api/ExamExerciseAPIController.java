package com.ptit.hackerthonservice.api;


import com.ptit.hackerthonservice.dto.ExamExerciseDTO;
import com.ptit.hackerthonservice.dto.ResponseDTO;
import com.ptit.hackerthonservice.dto.SearchDTO;
import com.ptit.hackerthonservice.service.ExamExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/exam-exercise")
public class ExamExerciseAPIController {
    @Autowired
    private ExamExerciseService examExerciseService;

    @PostMapping("/")
    public ResponseDTO<ExamExerciseDTO> create(@RequestBody @Valid ExamExerciseDTO examExerciseDTO) throws IOException {
    	examExerciseService.create(examExerciseDTO);
        return ResponseDTO.<ExamExerciseDTO>builder().code(String.valueOf(HttpStatus.OK.value())).data(examExerciseDTO).build();
    }

    @PutMapping(value = "/")
    public ResponseDTO<Void> update(@RequestBody @Valid ExamExerciseDTO examExerciseDTO) throws IOException {
    	examExerciseService.update(examExerciseDTO);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseDTO<ExamExerciseDTO> get(@PathVariable(value = "id") int id) {
        return ResponseDTO.<ExamExerciseDTO>builder().code(String.valueOf(HttpStatus.OK.value()))
                .data(examExerciseService.get(id)).build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseDTO<Void> delete(@PathVariable(value = "id") int id) {
    	examExerciseService.delete(id);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }

    @DeleteMapping("/delete/all/{ids}")
    public ResponseDTO<Void> deleteAll(@PathVariable(value = "ids") List<Integer> ids) {
    	examExerciseService.deleteAll(ids);
        return ResponseDTO.<Void>builder().code(String.valueOf(HttpStatus.OK.value())).build();
    }


    @PostMapping("/search")
    public ResponseDTO<List<ExamExerciseDTO>> search(@RequestBody @Valid SearchDTO searchDTO) {
        return examExerciseService.find(searchDTO);
    }

}
