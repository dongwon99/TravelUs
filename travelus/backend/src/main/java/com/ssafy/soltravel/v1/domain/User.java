package com.ssafy.soltravel.v1.domain;

import com.ssafy.soltravel.v1.domain.Enum.Role;
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
import java.util.ArrayList;
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
    private List<GeneralAccount> generalAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    /*
     * 생성 메서드
     */
    public static User createUser(
        String name, String password, String email, String phone,
        String address, LocalDate birth, String profileImageUrl, String userKey
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
}
