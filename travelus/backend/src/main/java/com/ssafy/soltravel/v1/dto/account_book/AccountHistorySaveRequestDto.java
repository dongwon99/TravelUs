package com.ssafy.soltravel.v1.dto.account_book;


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
public class AccountHistorySaveRequestDto {

  @Schema(description = "가계부를 등록할 외화 계좌 번호", example = "0886438854898839")
  private String accountNo;

  @Schema(description = "현금 사용처", example = "Starbucks")
  private String store;

  @Schema(description = "총 사용 금액", example = "9.35")
  private Double paid;

  @Schema(description = "사용 일시", example = "2024-08-29T20:51:21")
  private LocalDateTime transactionAt;

  @Schema(description = "구매 품목 리스트")
  private List<Item> items;

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  static class Item {

    @Schema(description = "구매 품목 이름", example = "Tea")
    private String item;

    @Schema(description = "구매 품목 가격", example = "3.05")
    private Double price;

    @Schema(description = "구매 품목 수량", example = "1")
    private Integer quantity;
  }
}
