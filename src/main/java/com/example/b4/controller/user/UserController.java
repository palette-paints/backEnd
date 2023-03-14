package com.example.b4.controller.user;

import com.example.b4.auth.PrincipalDetails;
import com.example.b4.config.JwtTokenProvider;
import com.example.b4.dto.JwtTokenDto;
import com.example.b4.dto.RequestTokenDto;
import com.example.b4.dto.user.UserJoinDto;
import com.example.b4.entity.RefreshToken;
import com.example.b4.entity.Role;
import com.example.b4.entity.User;
import com.example.b4.repository.RefreshTokenRepository;
import com.example.b4.repository.UserRepository;
import com.example.b4.service.user.UserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserTokenService userTokenService;

    @PostMapping("/login/form")
    public JwtTokenDto login(@RequestBody UserJoinDto user) {
        User member = userRepository.findByLoginId(user.getLoginId())
                .orElseThrow(() -> new IllegalStateException());
        JwtTokenDto jwtTokenDto = new JwtTokenDto();
        if (!passwordEncoder.matches(user.getLoginPw(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        RefreshToken oldRefreshToken = refreshTokenRepository.findByEmail(member.getEmail())
                .orElseGet(RefreshToken::new);
        oldRefreshToken.setEmail(member.getEmail());

        if(oldRefreshToken == null) {
            oldRefreshToken.setToken(jwtTokenProvider.createRefreshToken(member.getEmail(),member.getRole()));
            refreshTokenRepository.save(oldRefreshToken);
            jwtTokenDto.updateRefreshToken(oldRefreshToken);
        }

        else {
            jwtTokenDto.setAccessToken(jwtTokenProvider.createToken(member.getEmail(), member.getRole()));
            jwtTokenDto.setRefreshToken(jwtTokenProvider.createRefreshToken(member.getEmail(), member.getRole()));
            jwtTokenDto.setDate(jwtTokenProvider.jwtValidDate());
            RefreshToken refreshToken = new RefreshToken(member.getEmail(), jwtTokenDto.getRefreshToken());
            oldRefreshToken.updateToken(refreshToken.getToken());
            refreshTokenRepository.save(oldRefreshToken);
        }

        return jwtTokenDto;
    }

    @PostMapping("/signup")
    public ResponseEntity join(@RequestBody UserJoinDto userInfo){

        String encodePwd = bCryptPasswordEncoder.encode(userInfo.getLoginPw());

        User user = User.userDetailRegister()
                        .username(userInfo.getUserName()).email(userInfo.getUserEmail())
                        .role(Role.ROLE_USER).password(encodePwd).userBirth(userInfo.getUserBirth())
                        .userJob(userInfo.getUserJob()).userPhone(userInfo.getUserPhone()).provider("form")
                        .loginId(userInfo.getLoginId()).nickname(userInfo.getNickname()).build();

        userRepository.save(user);  //반드시 패스워드 암호화해야함
        return new ResponseEntity("회원가입 성공", HttpStatus.OK);
    }


    //토큰 재발급
    @PostMapping("/reissue")
    public JwtTokenDto refreshToken(@RequestHeader(value = "ACCESS-TOKEN") String accessToken,
                                    @RequestHeader(value = "REFRESH-TOKEN") String refreshToken ) {
        RequestTokenDto requestTokenDto = new RequestTokenDto(accessToken, refreshToken);

        JwtTokenDto jwtTokenDto = userTokenService.reissue(requestTokenDto);

        return jwtTokenDto;
    }



    // !!!! OAuth로 로그인 시 이 방식대로 하면 CastException 발생함
    @GetMapping("/form/loginInfo")
    @ResponseBody
    public String formLoginInfo(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();
        System.out.println(user);
        //User(id=2, username=11, password=$2a$10$m/1Alpm180jjsBpYReeml.AzvGlx/Djg4Z9/JDZYz8TJF1qUKd1fW, email=11@11, role=ROLE_USER, createTime=2022-01-30 19:07:43.213, provider=null, providerId=null)

        User user1 = principalDetails.getUser();
        System.out.println(user1);
        //User(id=2, username=11, password=$2a$10$m/1Alpm180jjsBpYReeml.AzvGlx/Djg4Z9/JDZYz8TJF1qUKd1fW, email=11@11, role=ROLE_USER, createTime=2022-01-30 19:07:43.213, provider=null, providerId=null)
        //user == user1

        return user.toString();
    }

    @GetMapping("/oauth/loginInfo")
    @ResponseBody
    public String oauthLoginInfo(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2UserPrincipal){
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println(attributes);
        // PrincipalOauth2UserService의 getAttributes내용과 같음

        Map<String, Object> attributes1 = oAuth2UserPrincipal.getAttributes();
        // attributes == attributes1

        return attributes.toString();     //세션에 담긴 user가져올 수 있음음
    }


    @GetMapping("/loginInfo")
    @ResponseBody
    public String loginInfo(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails){
        String result = "";

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        if(principal.getUser().getProvider() == null) {
            result = result + "Form 로그인 : " + principal;
        }else{
            result = result + "OAuth2 로그인 : " + principal;
        }
        return result;
    }

}