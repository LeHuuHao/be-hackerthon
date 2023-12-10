package com.ptit.hackerthonservice.dto;


import lombok.Data;

@Data
public class ExamExerciseDTO {
    private Integer id;
    private ExamDTO exam;
    private ExerciseDTO exercise;

}