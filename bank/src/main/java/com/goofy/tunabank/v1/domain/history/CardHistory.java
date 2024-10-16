package com.goofy.tunabank.v1.domain.history;

import com.goofy.tunabank.v1.domain.Card;
import com.goofy.tunabank.v1.domain.Currency;
import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import com.goofy.tunabank.v1.domain.Merchant;
import com.goofy.tunabank.v1.domain.MoneyBox;
import com.goofy.tunabank.v1.dto.card.CardPaymentRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("C")
public class CardHistory extends AbstractHistory {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    public Merchant merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "applied_exchange_rate")
    private Double exchangeRate;

    @Column
    private Double wonAmount;

    @Column
    private String transactionId;

    /*
     * 생성 메서드
     */
    public static CardHistory createCardHistory(
        Currency currency, Double amount, Double balance, Double wonAmount, Merchant merchant, Card card, String transactionId
    ) {

        CardHistory cardHistory = new CardHistory();
        cardHistory.currency = currency;
        cardHistory.amount = amount;
        cardHistory.balance = balance;
        cardHistory.wonAmount = wonAmount;
        cardHistory.merchant = merchant;
        cardHistory.card = card;
        cardHistory.exchangeRate = currency.getExchangeRate();
        cardHistory.transactionId = transactionId;
        cardHistory.transactionAt = LocalDateTime.now();
        cardHistory.transactionType = TransactionType.CW;
        return cardHistory;
    }

    public static CardHistory createCardHistory(
        Long nextId, Card card, MoneyBox moneyBox, Merchant merchant, CardPaymentRequestDto request, Double wonAmount
    ) {

        CardHistory cardHistory = new CardHistory();
        Currency currency = moneyBox.getCurrency();

        cardHistory.id = nextId;
        cardHistory.currency = currency;
        cardHistory.amount = request.getPaymentBalance();
        cardHistory.balance = moneyBox.getBalance();
        cardHistory.wonAmount = wonAmount;
        cardHistory.merchant = merchant;
        cardHistory.card = card;
        cardHistory.exchangeRate = currency.getExchangeRate();
        cardHistory.transactionId = request.getTransactionId();
        cardHistory.transactionAt = LocalDateTime.now();
        cardHistory.transactionType = TransactionType.CW;
        return cardHistory;
    }
}
