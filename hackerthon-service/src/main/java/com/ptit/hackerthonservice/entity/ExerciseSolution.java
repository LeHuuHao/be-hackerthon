package com.ptit.hackerthonservice.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class ExerciseSolution {
    @Id
    @Column(insertable = false)
    private Long solutionId;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @MapsId
    private Solution solution;

    @ManyToOne
    private Exercise exercise;
}
