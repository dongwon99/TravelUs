package com.ssafy.soltravel.v2.dto.transaction.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "페이징된 거래 내역 응답")
public class PagedHistoryResponse {
  @Schema(description = "현재 페이지 번호")
  private int pageNumber;

  @Schema(description = "페이지 크기")
  private int pageSize;

  @Schema(description = "현재 페이지까지 전체 페이지 수")
  private int totalPages;

  @Schema(description = "현재 페이지까지 전체 데이터 수")
  private long totalElements;

  @Schema(description = "현재 페이지가 첫 번째 페이지인지 여부")
  private boolean first;

  @Schema(description = "현재 페이지가 마지막 페이지인지 여부")
  private boolean last;

  @Schema(description = "현재 페이지의 데이터 수")
  private int numberOfElements;

  @Schema(description = "정렬 정보")
  private CustomSort sort; // Spring의 Sort 대신 Swagger 문서화를 위한 CustomSort 사용

  @ArraySchema(schema = @Schema(implementation = HistoryResponseDto.class))
  private List<HistoryResponseDto> content;
}

// Swagger에서 정렬 정보를 표현하기 위한 커스텀 Sort 클래스
@Schema(description = "정렬 정보")
class CustomSort {
  @Schema(description = "정렬이 비어 있는지 여부")
  private boolean empty;

  @Schema(description = "정렬이 되었는지 여부")
  private boolean sorted;

  @Schema(description = "정렬이 되지 않았는지 여부")
  private boolean unsorted;
}
