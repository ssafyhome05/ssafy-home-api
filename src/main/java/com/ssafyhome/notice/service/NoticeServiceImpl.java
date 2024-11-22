package com.ssafyhome.notice.service;

import com.ssafyhome.notice.dao.NoticeMapper;
import com.ssafyhome.notice.dto.NoticeDto;
import com.ssafyhome.notice.dto.NoticeListDto;
import com.ssafyhome.notice.entity.NoticeEntity;
import com.ssafyhome.common.util.ConvertUtil;
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
  public void addNotice(NoticeDto noticeDto) {

    NoticeEntity noticeEntity = convertUtil.convert(noticeDto, NoticeEntity.class);
    noticeMapper.insert(noticeEntity);
  }

  @Override
  public NoticeDto getNotice(long noticeSeq) {

    NoticeEntity noticeEntity = noticeMapper.selectBySeq(noticeSeq);

    return convertUtil.convert(noticeEntity, NoticeDto.class);
  }

  @Override
  public NoticeListDto getNotices(int page, int size) {

    List<NoticeEntity> noticeEntityList = noticeMapper.selectByPage((page - 1) * size, size);

    return new NoticeListDto(
        noticeMapper.getTotalRows(),
        convertUtil.convert(noticeEntityList, NoticeDto.class)
    );
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
