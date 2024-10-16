package com.ssafy.soltravel.v1.dto.account_book;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptUploadResponseDto {

  @Schema(description = "응답 메세지", example = "영수증 사진 업로드 완료")
  String message;

  @Schema(description = "업로드 URL", example = "https://my-s3-mingyu.s3.ap-southeast-2.amazonaws.com/test/2/67d8fd0b-9d14-4010-a8f3-125e7f4735ef%ED%95%98%EB%A9%B4%ED%95%98%EC%A7%80.png")
  String uploadUrl;


  // reciept
  // varchar(30) store
  // 1:many item
  // varchar(30) total_price

  // item
  // varchar(30) name
  // varchar(30) price


  //store
  //List<Item> items
  //String totalPrice

  //static class Item
  // String name
  // String price
}
