package com.goofy.tunabank.v1.controller;


import com.goofy.tunabank.v1.common.RecWrapper;
import com.goofy.tunabank.v1.dto.card.CardRequestDto;
import com.goofy.tunabank.v1.dto.card.CardResponseDto;
import com.goofy.tunabank.v1.dto.cardproduct.CardProductResponseDto;
import com.goofy.tunabank.v1.service.CardProductService;
import com.goofy.tunabank.v1.util.LogUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    return new ResponseEntity(new RecWrapper<>(response), HttpStatus.OK);
  }

}
