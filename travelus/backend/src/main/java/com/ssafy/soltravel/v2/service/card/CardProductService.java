package com.ssafy.soltravel.v2.service.card;


import com.ssafy.soltravel.v2.common.BankHeader;
import com.ssafy.soltravel.v2.dto.cardproduct.CardProductRequestDto;
import com.ssafy.soltravel.v2.dto.cardproduct.CardProductResponseDto;
import com.ssafy.soltravel.v2.util.WebClientUtil;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CardProductService {

  private static final String DEFAULT_URI = "/card-product";

  private final WebClientUtil webClientUtil;
  private final Map<String, String> apiKeys;

  public List<CardProductResponseDto> getCardProducts() {
    CardProductRequestDto request = CardProductRequestDto.builder()
        .header(
            BankHeader.createHeader(
                apiKeys.get("API_KEY"),
                ""
            )
        )
        .build();

    ResponseEntity<Map<String, Object>> response = webClientUtil.request(
        DEFAULT_URI+"/list", request, CardProductRequestDto.class
    );

    // REC 필드를 파싱하여 CardProductResponseDto 리스트로 변환
    List<Map<String, Object>> recObjectList = (List<Map<String, Object>>) response.getBody().get("REC");

    List<CardProductResponseDto> cardProductList = recObjectList.stream()
        .map(recObject -> {
          CardProductResponseDto cardProduct = new CardProductResponseDto();
          cardProduct.setCardName((String) recObject.get("cardName"));
          cardProduct.setCardDescription((String) recObject.get("cardDescription"));
          cardProduct.setCardUniqueNo((String) recObject.get("cardUniqueNo"));
          return cardProduct;
        })
        .collect(Collectors.toList());

    return cardProductList;
  }



}
