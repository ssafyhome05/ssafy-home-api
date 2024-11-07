package com.ssafyhome.bookmark.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface BookmarkMapper {
    void addBookmark(Map<String, Object> params);
    void deleteBookmark(Map<String, Object> params);
    void addLocationBookmark(Map<String, Object> params);
    void deleteLocationBookmark(Map<String, Object> params);
}
