package com.ssafyhome.model.dao.mapper;

import com.ssafyhome.model.entity.mysql.NoticeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {

  NoticeEntity selectBySeq(long noticeSeq);
  List<NoticeEntity> selectByPage(int page);
  void insert(NoticeEntity noticeEntity);
  void update(NoticeEntity noticeEntity);
  void deleteBySeq(long noticeSeq);
}
