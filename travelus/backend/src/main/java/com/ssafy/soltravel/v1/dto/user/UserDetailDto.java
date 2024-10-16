package com.ssafy.soltravel.v1.dto.user;

import com.ssafy.soltravel.v1.domain.Enum.Role;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@AllArgsConstructor
public class UserDetailDto implements UserDetails {

  private Long id;
  private String name;
  private String email;
  private String password;
  private Role role;

  //임시
  public UserDetailDto() {
    name = "userTest";
    email = "emailTest@email.com";
    password = "passwordTest";
    role = Role.USER;
  }

  /*
   * UserDetails 인터페이스 메서드
   * 해당 User의 권한 return
   * SecutiryFilterChain에서 권한을 체크할 때 사용됨
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    authorities.add(new GrantedAuthority() {
      @Override
      public String getAuthority() {
        return String.valueOf(role);
      }
    });
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return name;
  }

  @Override
  public boolean isAccountNonExpired() {
    //return UserDetails.super.isAccountNonExpired();
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    //return UserDetails.super.isAccountNonLocked();
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    //return UserDetails.super.isCredentialsNonExpired();
    return true;
  }

  @Override
  public boolean isEnabled() {
    //return UserDetails.super.isEnabled();
    return true;
  }
}
