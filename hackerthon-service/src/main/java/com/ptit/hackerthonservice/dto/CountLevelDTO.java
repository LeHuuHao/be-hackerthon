package com.ptit.hackerthonservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountLevelDTO {
    Long countEasy;

    Long countMedium;

    Long countHard;

    Long countTryHard;
}
