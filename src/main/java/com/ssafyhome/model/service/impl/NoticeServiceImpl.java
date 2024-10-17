package com.ssafyhome.model.service.impl;

import com.ssafyhome.model.dao.mapper.NoticeMapper;
import com.ssafyhome.model.dto.NoticeDto;
import com.ssafyhome.model.entity.mysql.NoticeEntity;
import com.ssafyhome.model.service.NoticeService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

  private final NoticeMapper noticeMapper;

  public NoticeServiceImpl(NoticeMapper noticeMapper) {

    this.noticeMapper = noticeMapper;
  }

  @Override
  public void registerNotice(NoticeDto noticeDto) {

    NoticeEntity noticeEntity = NoticeEntity.builder()
        .noticeTitle(noticeDto.getNoticeTitle())
        .noticeContent(noticeDto.getNoticeContent())
        .build();

    noticeMapper.insert(noticeEntity);
  }

  @Override
  public NoticeDto getNotice(long noticeSeq) {

    NoticeEntity noticeEntity = noticeMapper.selectBySeq(noticeSeq);

    return convertNoticeEntityToDto(noticeEntity);
  }

  @Override
  public List<NoticeDto> getNotices(int page) {

    List<NoticeEntity> noticeEntityList = noticeMapper.selectByPage(page);

    return noticeEntityList.stream()
        .map(this::convertNoticeEntityToDto)
        .toList();
  }

  private NoticeDto convertNoticeEntityToDto(NoticeEntity noticeEntity) {

    String title = noticeEntity.getNoticeTitle();

    if (noticeEntity.getModifiedAt() != null) {

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      String modifiedString = noticeEntity.getModifiedAt().format(formatter);
      title = title + " " + modifiedString;
    }

    return NoticeDto.builder()
        .noticeSeq(noticeEntity.getNoticeSeq())
        .noticeTitle(title)
        .noticeContent(noticeEntity.getNoticeContent())
        .createdAt(noticeEntity.getCreatedAt())
        .build();
  }

  @Override
  public void updateNotice(long noticeSeq, NoticeDto noticeDto) {

    NoticeEntity noticeEntity = NoticeEntity.builder()
        .noticeSeq(noticeSeq)
        .noticeTitle(noticeDto.getNoticeTitle())
        .noticeContent(noticeDto.getNoticeContent())
        .build();

    noticeMapper.update(noticeEntity);
  }

  @Override
  public void deleteNotice(long noticeSeq) {

    noticeMapper.deleteBySeq(noticeSeq);
  }
}
