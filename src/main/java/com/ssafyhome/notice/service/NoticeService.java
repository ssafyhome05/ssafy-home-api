package com.ssafyhome.notice.service;

import com.ssafyhome.notice.dto.NoticeDto;

import java.util.List;

public interface NoticeService {

  void registerNotice(NoticeDto noticeDto);
  NoticeDto getNotice(long noticeSeq);
  List<NoticeDto> getNotices(int page);
  void updateNotice(long noticeSeq, NoticeDto noticeDto);
  void deleteNotice(long noticeSeq);
}
