package com.ssafy.soltravel.v1.mapper;

import com.google.gson.Gson;
import com.ssafy.soltravel.v1.dto.account_book.ReceiptAnalysisDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountBookMapper {

  public static ReceiptAnalysisDto convertJSONToItemAnalysisDto(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, ReceiptAnalysisDto.class);
  }

}
