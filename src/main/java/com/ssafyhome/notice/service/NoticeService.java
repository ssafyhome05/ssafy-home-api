package com.ssafyhome.notice.service;

import com.ssafyhome.notice.dto.NoticeDto;
import com.ssafyhome.notice.dto.NoticeListDto;

import java.util.List;

public interface NoticeService {

  void addNotice(NoticeDto noticeDto);
  NoticeDto getNotice(long noticeSeq);
  NoticeListDto getNotices(int page, int size);
  void updateNotice(long noticeSeq, NoticeDto noticeDto);
  void deleteNotice(long noticeSeq);
}
