package com.ssafyhome.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NoticeListDto {

  private int total;
  private List<NoticeDto> content;
}
