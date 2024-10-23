package com.ssafyhome.model.service.impl;

import com.ssafyhome.model.dao.mapper.NoticeMapper;
import com.ssafyhome.model.dto.notice.NoticeDto;
import com.ssafyhome.model.entity.mysql.NoticeEntity;
import com.ssafyhome.model.service.NoticeService;
import com.ssafyhome.util.ConvertUtil;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

  private final NoticeMapper noticeMapper;
  private final ConvertUtil convertUtil;

  public NoticeServiceImpl(
      NoticeMapper noticeMapper,
      ConvertUtil convertUtil
  ) {

    this.noticeMapper = noticeMapper;
    this.convertUtil = convertUtil;
  }

  @Override
  public void registerNotice(NoticeDto noticeDto) {

    NoticeEntity noticeEntity = convertUtil.convert(noticeDto, NoticeEntity.class);
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

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("(수정: yyyy-MM-dd HH:mm:ss)");
      String modifiedString = noticeEntity.getModifiedAt().format(formatter);
      title = title + " " + modifiedString;
    }

    NoticeDto noticeDto = convertUtil.convert(noticeEntity, NoticeDto.class);
    noticeDto.setNoticeTitle(title);
    return noticeDto;
  }

  @Override
  public void updateNotice(long noticeSeq, NoticeDto noticeDto) {

    NoticeEntity noticeEntity = convertUtil.convert(noticeDto, NoticeEntity.class);
    noticeMapper.update(noticeEntity);
  }

  @Override
  public void deleteNotice(long noticeSeq) {

    noticeMapper.deleteBySeq(noticeSeq);
  }
}
