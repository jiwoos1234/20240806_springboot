package com.example.ex6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
  private Long mno;
  private String title;
  @Builder.Default // @AllArgsConstructor가 없으면 에러,기본값초기화
  private List<MovieImageDTO> imageDTOList = new ArrayList<>();
  private double avg;
  private Long reviewCnt;
  private LocalDateTime regDate;
  private LocalDateTime modDate;
}
