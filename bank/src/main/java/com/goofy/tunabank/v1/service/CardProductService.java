package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.CardProduct;
import com.goofy.tunabank.v1.dto.cardproduct.CardProductResponseDto;
import com.goofy.tunabank.v1.mapper.CardProductMapper;
import com.goofy.tunabank.v1.repository.CardProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CardProductService {

  private final CardProductRepository cardProductRepository;

  public List<CardProductResponseDto> getCardProducts() {
    List<CardProduct> cardProducts = findAll();

    return cardProducts.stream()
        .map(CardProductMapper.INSTANCE::cpToCardProductResponseDto)
        .toList();
  }


  public List<CardProduct> findAll() {
    return cardProductRepository.findAll();
  }

}
