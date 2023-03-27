package com.example.b4.controller.user;

import com.example.b4.config.JwtTokenProvider;
import com.example.b4.dto.user.MypageDto;
import com.example.b4.repository.UserRepository;
import com.example.b4.service.user.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MypageService mypageService;

    @GetMapping("")
    public MypageDto getUserInfo (ServletRequest request) {
        return mypageService.getUserInfo(getEmail(request));
    }

    @PutMapping("")
    public ResponseEntity updateUserInfo(ServletRequest request, @RequestBody MypageDto mypageDto) {
        return mypageService.updateUserInfo(getEmail(request), mypageDto);
    }

    private String getEmail(ServletRequest request){
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        return jwtTokenProvider.getUserPk(token);
    }

}
