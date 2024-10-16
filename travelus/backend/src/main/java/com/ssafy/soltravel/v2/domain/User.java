package com.ssafy.soltravel.v2.domain;

import com.ssafy.soltravel.v2.domain.Enum.Gender;
import com.ssafy.soltravel.v2.domain.Enum.Role;
import com.ssafy.soltravel.v2.dto.user.UserUpdateRequestDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String address;

    @Column
    private LocalDate birth;

    @Column(name = "register_at")
    private LocalDateTime registerAt;

    @Column(name = "is_exit")
    private Boolean isExit;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "user_key")
    private String userKey;

    @Column(name = "profile")
    private String profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /*
     * 생성 메서드
     */
    public static User createUser(
        String name, String password, String email, String phone,
        String address, LocalDate birth, String profileImageUrl, String userKey, Gender gender
    ) {

        User user = new User();
        user.name = name;
        user.password = password;
        user.email = email;
        user.phone = phone;
        user.address = address;
        user.birth = birth;
        user.registerAt = LocalDateTime.now();
        user.role = Role.USER;
        user.isExit = false;

        user.profile = profileImageUrl;
        user.userKey = userKey;
        user.gender = gender;

        return user;
    }

    public static User createUser(
        String name, String password, String email, String phone,
        String address, LocalDate birth, String profileImageUrl
    ) {

        User user = new User();
        user.name = name;
        user.password = password;
        user.email = email;
        user.phone = phone;
        user.address = address;
        user.birth = birth;
        user.registerAt = LocalDateTime.now();
        user.role = Role.USER;
        user.isExit = false;
        user.profile = profileImageUrl;
        user.userKey = "";
        return user;
    }

    /*
     * 수정 메서드
     */
    public void updateProfile(String profileImageUrl) {
        this.profile = profileImageUrl;
    }

    public void update(UserUpdateRequestDto update) {
        this.name = update.getName() != null ? update.getName() : this.name;
        this.phone = update.getPhone() != null ? update.getPhone() : this.phone;
        this.address = update.getAddress() != null ? update.getAddress() : this.address;
        this.birth = update.getBirth() != null ? update.getBirth() : this.birth;
    }

    public void updatePwd(String pwd) {
        this.password = pwd;
    }


}
