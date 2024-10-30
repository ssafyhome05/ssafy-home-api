package com.ssafyhome.model.entity.mysql;

import lombok.Data;

@Data
public class CategoryEntity {

    private String categoryCode;
    private String categoryName;
    private boolean categoryType;
}
