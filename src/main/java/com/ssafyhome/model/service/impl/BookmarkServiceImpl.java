package com.ssafyhome.model.service.impl;

import com.ssafyhome.model.dao.mapper.BookmarkMapper;
import com.ssafyhome.model.service.BookmarkService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkMapper bookmarkMapper;

    public BookmarkServiceImpl(
            BookmarkMapper bookmarkMapper
    ) {
        this.bookmarkMapper = bookmarkMapper;
    }

    @Override
    public void addBookmark(Map<String, Object> params) {
        bookmarkMapper.addBookmark(params);
    }

    @Override
    public void deleteBookmark(Map<String, Object> params) {
        bookmarkMapper.deleteBookmark(params);
    }

    @Override
    public void addLocationBookmark(Map<String, Object> params) {
        bookmarkMapper.addLocationBookmark(params);
    }

    @Override
    public void deleteLocationBookmark(Map<String, Object> params) {
        bookmarkMapper.deleteLocationBookmark(params);
    }

}
