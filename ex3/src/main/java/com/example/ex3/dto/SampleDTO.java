package com.example.ex3.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Data
public class SampleDTO {
  private Long sno;
  private String first;
  private String last;
  private LocalDateTime regTime;
}
