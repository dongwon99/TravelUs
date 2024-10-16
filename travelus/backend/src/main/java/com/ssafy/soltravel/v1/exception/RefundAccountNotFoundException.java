package com.ssafy.soltravel.v1.exception;

public class RefundAccountNotFoundException extends RuntimeException {

    public RefundAccountNotFoundException() {
        super("잔액이 있는 계좌 해지는 환불 계좌가 필수입니다.");
    }
}
