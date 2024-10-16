package com.ssafy.soltravel.v2.dto.account_book;

import com.ssafy.soltravel.v2.dto.account_book.AccountHistorySaveRequestDto.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailAccountHistoryReadResponseDto {

  @Schema(description = "가맹점 이름", example = "Starbucks")
  private String store;

  @Schema(description = "거래 상대 이름", example = "허동원")
  private String payeeName;

  @Schema(description = "주소", example = "169 Euston Road, London")
  private String address;

  @Schema(description = "거래 요약", example = "월급 들어옴")
  private String transactionSummary;

  @Schema(description = "총 사용 금액", example = "9.35")
  private Double paid;

  @Schema(description = "거래 통화 코드", example = "USD")
  private String currency;

  @Schema(description = "사용 일시", example = "2024-08-29T20:51:21")
  private LocalDateTime transactionAt;


  @Schema(description = "구매 품목 리스트")
  private List<Item> items;

}
