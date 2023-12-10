package com.ptit.hackerthonservice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class RankingExam extends CreateAuditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long score;

    @OneToOne
    private Exam exam;

    @Formula("(SELECT COUNT(a.id) FROM answer a WHERE a.created_by_id = created_by_id)")
    private Long answerNo;

    @Formula("(SELECT COUNT(a.id) FROM answer a WHERE a.created_by_id = created_by_id AND a.status_code = 100)")
    private Long acceptedNo;

}
