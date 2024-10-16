package com.goofy.tunabank.v1.domain;

import com.goofy.tunabank.v1.domain.Enum.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(name = "name")
  private String name;

  @Column(nullable = false, name = "credentialId")
  private String credentialId;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  List<Key> keys = new ArrayList<>();

  @Column(name = "is_exit")
  private Boolean isExit;

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "create_at")
  private LocalDateTime createdAt;

  @Column(name = "update_at")
  private LocalDateTime updateAt;

  @Column(name = "exit_at")
  private LocalDateTime exitAt;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Account> accounts = new ArrayList<>();

  /*
   * 생성 메서드
   */
  public static User createUser(String credentialId, String name, Role role) {
    User user = new User();
    user.credentialId = credentialId;
    user.name = name;
    user.role = role;
    user.isExit = false;
    user.createdAt = LocalDateTime.now();
    user.updateAt = LocalDateTime.now();
    user.exitAt = null;
    return user;
  }

  /*
   * 유저 탈퇴 처리
   */
  public void deactivateUser() {
    this.isExit = true;
    this.exitAt = LocalDateTime.now();

    keys.forEach(Key::deactivateKey); // Key의 비활성화 메서드 호출
  }
  
  /*
  * 유저 이름 변경 
  */
  public void updateUserName(String newName) {
    this.name = newName;
  }

  /*
   * UserDetails 인터페이스 구현 메서드
   */
  @Override

  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(this.role.name()));
    return authorities;
  }

  @Override
  public String getUsername() {
    return this.credentialId;
  }

  // 비밀번호 필드가 없는 경우, 필요한 처리 추가
  @Override
  public String getPassword() {
    return null;
  }

  // 계정 만료 여부 처리 (true는 만료되지 않음을 의미)
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  // 계정 잠금 여부 처리 (true는 잠금되지 않음을 의미)
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  // 자격 증명 만료 여부 처리 (true는 만료되지 않음을 의미)
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  // 계정 활성화 여부 (탈퇴 여부에 따라 결정)
  @Override
  public boolean isEnabled() {
    return !this.isExit;
  }
}
