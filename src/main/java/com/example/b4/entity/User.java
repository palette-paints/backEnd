package com.example.b4.entity;

import com.example.b4.dto.user.MypageDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.security.Timestamp;

@Entity
@Table(name = "users")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @Column(name = "userId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String loginId;

    @Setter @Column(name = "loginPw")
    private String password;

    @Column(name = "userEmail")
    private String email;

    @Column(name = "userRole")
    @Enumerated(EnumType.STRING)
    @Setter
    private Role role;

    private String userPhone;

    private String  userBirth;

    private String userJob;

    private String nickname;


    private String provider;    // oauth2를 이용할 경우 어떤 플랫폼을 이용하는지
    private String providerId;  // oauth2를 이용할 경우 아이디값


    @Builder(builderClassName = "UserDetailRegister", builderMethodName = "userDetailRegister")
    public User(String username, String password, String email, Role role,
                String loginId, String userPhone, String userBirth, String userJob,
                String nickname, String provider) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.loginId = loginId;
        this.userPhone = userPhone;
        this.userBirth = userBirth;
        this.userJob = userJob;
        this.nickname = nickname;
        this.provider = provider;

    }

    @Builder(builderClassName = "OAuth2Register", builderMethodName = "oauth2Register")
    public User(String username, String password, String email, Role role, String provider, String providerId,
                String nickname, String userBirth, String userPhone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.userBirth = userBirth;
        this.userPhone = userPhone;
    }

    public void updateUserInfo(MypageDto mypageDto){
        this.userBirth = mypageDto.getUserBirth();
        this.userJob = mypageDto.getUserJob();
        this.role = mypageDto.getUserRole();
        this.username = mypageDto.getUserName();
        this.userPhone = mypageDto.getUserPhone();
        this.email = mypageDto.getEmail();
    }
}