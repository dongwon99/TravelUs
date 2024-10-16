package com.goofy.tunabank.v1.domain;

import com.goofy.tunabank.v1.domain.Enum.KeyStatus;
import com.goofy.tunabank.v1.domain.Enum.KeyType;
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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "api_key")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Key {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column
  @Enumerated(EnumType.STRING)
  private KeyType type;

  @Column
  private String value;

  @Column
  @Enumerated(EnumType.STRING)
  private KeyStatus status;

  @Column(name = "create_at")
  private LocalDateTime createAt;

  @Column(name = "expire_at")
  private LocalDateTime expireAt;


  /*
  * 연관관계 편의 메서드
  */
  public void setUser(User user) {
    this.user = user;
    user.getKeys().add(this);
  }


  /*
  *  생성 메서드
  */
  public static Key createKey(User user, KeyType type, String value) {
    Key key = new Key();
    key.setUser(user);
    key.type = type;
    key.value = value;

    key.status = KeyStatus.ACTIVE;
    key.createAt = LocalDateTime.now();
    key.expireAt = LocalDateTime.now().plusYears(1);
    return key;
  }

  /*
  * 키 갱신 메서드
  */
  public Key updateKey(String keyValue) {
    this.value = keyValue;
    this.status = KeyStatus.ACTIVE;
    this.createAt = LocalDateTime.now();
    this.expireAt = LocalDateTime.now().plusYears(1);
    return this;
  }

  public void deactivateKey() {
    this.status = KeyStatus.INACTIVE;
  }
}
