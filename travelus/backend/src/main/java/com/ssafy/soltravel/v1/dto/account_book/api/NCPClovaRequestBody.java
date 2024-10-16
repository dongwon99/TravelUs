package com.ssafy.soltravel.v1.dto.account_book.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NCPClovaRequestBody {

  /*
  * version : V2로 고정
  * requestId: UUID
  * timestamp : 현재 시간
  * lang : 'ja' 로 고정(일본어)
  * */
  private String version = "v2";
  private String requestId = UUID.randomUUID().toString();
  private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
  private String lang;
  private List<Image> images = new ArrayList<>();

  /**
   * Image 객체를 생성하고 리스트에 추가하는 메서드
   *
   * @param format 이미지 포맷 (예: "png")
   * @param url 이미지 URL
   * @param data 이미지 데이터 (Base64 인코딩된 문자열 등)
   * @param name 이미지 이름
   */
  public void addImage(String data, String url, String format, String name, String lang) {
    Image image = new Image(format, url, data, name);
    this.lang = lang;
    this.images.add(image);
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Image {
    private String format; // 이미지 포맷 (예: "png")
    private String url;    // 이미지 URL
    private String data;   // 이미지 데이터 (Base64 등)
    private String name;   // 이미지 이름
  }
}
