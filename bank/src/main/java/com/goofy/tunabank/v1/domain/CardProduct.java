package com.goofy.tunabank.v1.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "card_product")
@Getter
public class CardProduct {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cardProductId;

  @Column
  private String cardName;

  @Column
  private String cardDescription;

  @Column(length = 16)
  private String cardUniqueNo;
}
