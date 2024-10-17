package com.ssafyhome.model.service.impl;

import com.ssafyhome.model.dto.NoticeDto;
import com.ssafyhome.model.service.NoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

  @Override
  public void registerNotice(NoticeDto noticeDto) {

  }

  @Override
  public NoticeDto getNotice(long noticeSeq) {
    return null;
  }

  @Override
  public List<NoticeDto> getNotices(int page) {
    return List.of();
  }

  @Override
  public void updateNotice(NoticeDto noticeDto) {

  }

  @Override
  public void deleteNotice(long noticeSeq) {

  }
}
