package com.ssafy.soltravel.v2.domain.Enum;

public enum NotificationType {
    PT, // 개인 거래(입출금 & 이체) : Personal Transaction
    GT, // 그룹 거래(입출금 & 이체) Group Transaction
    E, // 환전 (자동 & 일반 환전) Exchange
    S, // 정산 Settlement,
    GD // 그룹계좌 디테일로 보내기
}
