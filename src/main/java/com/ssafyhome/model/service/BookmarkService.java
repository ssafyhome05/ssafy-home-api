package com.ssafyhome.model.service;

import java.util.Map;

public interface BookmarkService {
    void addBookmark(Map<String, Object> params);
    void deleteBookmark(Map<String, Object> params);
    void addLocationBookmark(Map<String, Object> params);
    void deleteLocationBookmark(Map<String, Object> params);
}
