package com.ssafy.soltravel.v1.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
public class LatestRate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "latest_rate_id")
  @Schema(description = "최신 환율 Log ID", example = "1")
  private Long id;

  @Column(name = "post_at")
  @Schema(description = "환율 게시 날짜", example = "2024-08-26")
  private LocalDate postAt;

  @Column(name = "deal_bas_r")
  @Schema(description = "매매 기준율", example = "1326.90")
  private Double dealBasR;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  @JoinColumn(name = "currency_id")
  @Schema(description = "통화 정보", hidden = true) // 연관 엔티티를 숨기고 싶을 때
  private Currency currency;

  @Column(name = "ttb")
  @Schema(description = "전신환(송금) 받으실 때 환율", example = "1314.20")
  private Double ttb;

  @Column(name = "tts")
  @Schema(description = "전신환(송금) 보내실 때 환율", example = "1339.60")
  private Double tts;

  @Column(name = "cash_buying")
  @Schema(description = "현찰 매입 시 환율(파실 때)", example = "1303.68")
  private Double cashBuying;

  @Column(name = "cash_selling")
  @Schema(description = "현찰 매도 시 환율(사실 때)", example = "1350.12")
  private Double cashSelling;

  @Column(name = "tc_buying")
  @Schema(description = "여행자 수표 매입 시 환율(사실 때)", example = "1342.82")
  private Double tcBuying;
}
