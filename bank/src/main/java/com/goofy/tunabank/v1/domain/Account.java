package com.goofy.tunabank.v1.domain;

import com.goofy.tunabank.v1.domain.Enum.AccountType;
import com.goofy.tunabank.v1.dto.account.request.CreateGeneralAccountRequestDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    private String accountPassword;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MoneyBox> moneyBoxes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "account")
    private List<Card> cards = new ArrayList<>();

    private String status;

    // ==== 엔티티 생성 메서드 ====
    public static Account createAccount(CreateGeneralAccountRequestDto requestDto, Bank bank, User user) {

        Account account = Account.builder()
            .user(user)
            .bank(bank)
            .accountType(requestDto.getAccountType())
            .accountNo(createAccountNumber(requestDto.getAccountType()))  // 계좌 번호 생성
            .accountPassword(requestDto.getAccountPassword())
            .moneyBoxes(new ArrayList<>())
            .createdAt(requestDto.getHeader().getTransmissionDateTime())
            .updatedAt(requestDto.getHeader().getTransmissionDateTime())
            .status("ACTIVE")
            .build();

        return account;
    }

    // 계좌 번호 생성 로직
    private static String createAccountNumber(AccountType accountType) {

        // 계좌 종류 고유값
        final String code = accountType.getCode();

        // 랜덤 7자리 숫자 생성 (검증 번호는 마지막에 추가)
        String randomPart = String.format("%07d", (int) (Math.random() * 10000000));

        // 검증 번호를 계산하기 위한 계좌 번호 생성
        String accountNumberWithoutCheckDigit = code + randomPart;

        // 검증 번호 계산
        int checkDigit = calculateLuhnCheckDigit(accountNumberWithoutCheckDigit);

        // 지점 번호 -> 209로 고정
        String branchNumber = "209";

        return String.format("%s-%s%d-%s", code, randomPart, checkDigit, branchNumber);
    }

    // Luhn 알고리즘을 사용한 검증 번호 생성 로직
    private static int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = false;

        // 오른쪽에서 왼쪽으로 숫자를 처리
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        // 10으로 나누어 떨어지게 하는 숫자 반환
        return (10 - (sum % 10)) % 10;
    }

    public void closeAccount() {
        if (!this.status.equals("CLOSED")) {
            this.status = "CLOSED";
        }
    }

    public void addMoneyBox(MoneyBox moneyBox) {
        this.moneyBoxes.add(moneyBox);
    }

}
