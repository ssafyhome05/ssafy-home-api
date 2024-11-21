package com.ssafyhome.house.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum AdmCode {
  SEOUL(11, "서울특별시"),
  BUSAN(21, "부산광역시"),
  DAEGU(22, "대구광역시"),
  INCHEON(23, "인천광역시"),
  GWANGJU(24, "광주광역시"),
  DAEJEON(25, "대전광역시"),
  ULSAN(26, "울산광역시"),
  SEJONG(29, "세종특별자치시"),
  GYEONGGI(31, "경기도"),
  GANGWON(32, "강원특별자치도"),
  CHUNGBUK(33, "충청북도"),
  CHUNGNAM(34, "충청남도"),
  JEONBUK(35, "전북특별자치도"),
  JEONNAM(36, "전라남도"),
  GYEONGBUK(37, "경상북도"),
  GYEONGNAM(38, "경상남도"),
  JEJU(39, "제주특별자치도");

  private final int code;
  private final String name;

  public static List<Integer> getAllCodes() {

    return Arrays.stream(AdmCode.values()).map(AdmCode::getCode).toList();
  }
}