package com.ptit.hackerthonservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticUserDTO {

    private  String displayName;

    private  String photoURL;

    private Long score;

    private Long submission;

    private Long AC;
}
