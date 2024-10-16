package com.goofy.tunabank.v1.domain;


import com.goofy.tunabank.v1.domain.history.CardHistory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Table(name = "card")
@Getter
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_product_id")
    private CardProduct cardProduct;

    @Column(length = 20)
    private String cardNo;

    @Column(length = 4)
    private String cvc;

    @Column
    private String password;

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime expireAt;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardHistory> history = new ArrayList<>();


    /*
     * 생성 메서드
     */
    public static Card createCard(Account account, CardProduct cardProduct, String cardNo, String cvc, String pwd) {
        Card card = new Card();
        card.account = account;
        card.cardProduct = cardProduct;
        card.cardNo = cardNo;
        card.cvc = cvc;
        card.createAt = LocalDateTime.now();
        card.expireAt = LocalDateTime.now().plusYears(5);
        card.password = pwd;
        return card;
    }


}
