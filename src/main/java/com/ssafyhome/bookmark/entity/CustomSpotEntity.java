package com.ssafyhome.bookmark.entity;

import lombok.Data;

@Data
public class CustomSpotEntity {

    private int spotSeq;
    private int userSeq;
    private String spotName;
    private String jibun;
    private String roadNm;
    private String latitude;
    private String longitude;
}
