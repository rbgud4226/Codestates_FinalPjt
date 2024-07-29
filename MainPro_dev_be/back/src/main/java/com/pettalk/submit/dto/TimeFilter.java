package com.pettalk.submit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TimeFilter {
    LocalDateTime startTIme;
    LocalDateTime endTime;
}
