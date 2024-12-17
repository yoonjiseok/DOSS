package com.example.moveon.domain;

import java.util.Date;


import com.example.moveon.domain.common.BaseTimeEntity;
import com.example.moveon.domain.enums.Gender;
import com.example.moveon.domain.enums.Provider;
import com.example.moveon.domain.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Pattern(regexp = "[가-힣a-zA-Z0-9]{3,10}", message = "닉네임은 한글, 영어, 숫자를 조합해 3글자 이상, 10글자 이하입니다.")
    private String nickname;

    private String profileImage;

    private Date birthYear;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Email
    private String email;

    private Date deletedAt;

    public void updateNickName(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void updateBirthYear(Date birthYear) {
        this.birthYear = birthYear;
    }

    public void updateGender(Gender gender) {
        this.gender = gender;
    }

    public void updateStatus(UserStatus status) {
        this.status = status;
    }

    public void updateEmail(String email) {
        this.email = email;
    }
}

