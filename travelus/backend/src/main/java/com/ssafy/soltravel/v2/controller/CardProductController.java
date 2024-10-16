package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.cardproduct.CardProductResponseDto;
import com.ssafy.soltravel.v2.service.card.CardProductService;
import com.ssafy.soltravel.v2.util.LogUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card-product")
@RequiredArgsConstructor
public class CardProductController {

  private final CardProductService cardProductService;

  @PostMapping("/list")
  public ResponseEntity getCardProducts(){
    LogUtil.info("카드 상품 조회 요청");
    List<CardProductResponseDto> response = cardProductService.getCardProducts();
    return new ResponseEntity(response, HttpStatus.OK);
  }

}
