package com.ptit.hackerthonservice.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "exam_exercise")
@Data
@EqualsAndHashCode(callSuper = true)
public class ExamExercise extends CreateAuditable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private Exam exam;

    @ManyToOne
    private Exercise exercise;

}
