package com.ssafy.soltravel.v2.dto.account_book;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptUploadRequestDto {

  @Schema(description = "영수증 사진", example = "file")
  MultipartFile file;

  @Schema(description = "사진 형식(jpg, png, pdf)", example = "jpg")
  String format;

  @Schema(description = "영수증 언어('', ja, kr)", example = "영어: ''(빈문자열), 한글: ko, 일본어: ja")
  String lang;
}
