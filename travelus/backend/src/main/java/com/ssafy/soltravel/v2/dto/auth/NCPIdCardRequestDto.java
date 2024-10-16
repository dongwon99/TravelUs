package com.ssafy.soltravel.v2.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
public class NCPIdCardRequestDto {

  private Message message;

  @JsonIgnore
  private MultipartFile file;

  public NCPIdCardRequestDto(String format, MultipartFile file){
    this.message = new Message(format);
    this.file = file;
  }

  @Data
  @Builder
  @AllArgsConstructor
  public static class Message {
    private String version;
    private String requestId;
    private long timestamp;
    private List<Image> images;

    public Message() {
      this.version = "v2";
      this.requestId = "1234";
      this.timestamp = System.currentTimeMillis();
      this.images = new ArrayList<>();
      images.add(new Image());
    }

    public Message(String format) {
      this.version = "v2";
      this.requestId = "1234";
      this.timestamp = System.currentTimeMillis();
      this.images = new ArrayList<>();
      images.add(new Image(format));
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Image {
      private String format;
      private String name;

      public Image() {
        this.format = "jpg";
        this.name = "covid_sample";
      }

      public Image(String format){
        this.format = format;
        this.name = "covid_sample";
      }
    }
  }



}
