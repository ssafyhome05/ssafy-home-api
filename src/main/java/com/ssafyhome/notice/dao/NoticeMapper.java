package com.ssafyhome.notice.dao;

import com.ssafyhome.notice.entity.NoticeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {

  NoticeEntity selectBySeq(long noticeSeq);
  List<NoticeEntity> selectByPage(
      @Param("startIdx") int startIdx,
      @Param("size") int size
  );
  void insert(NoticeEntity noticeEntity);
  void update(NoticeEntity noticeEntity);
  void deleteBySeq(long noticeSeq);
  int getTotalRows();
}
