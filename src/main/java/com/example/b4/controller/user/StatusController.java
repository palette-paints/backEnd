package com.example.b4.controller.user;

import com.example.b4.config.JwtTokenProvider;
import com.example.b4.dto.user.PlayStatusDto;
import com.example.b4.dto.user.StudyStatusDto;
import com.example.b4.entity.post.Play;
import com.example.b4.entity.post.PlayStatus;
import com.example.b4.repository.PostRepository;
import com.example.b4.service.user.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("status")
@RestController
@RequiredArgsConstructor
public class StatusController {
    private final JwtTokenProvider jwtTokenProvider;
    private final PostRepository postRepository;
    private final UserStatusService userStatusService;

    @GetMapping("/plays")
    public List<PlayStatusDto> getPlaysUser(ServletRequest request) {
        return userStatusService.getPlayStatus(getEmail(request));
    }

//    @GetMapping("/minds")
//    public List<PlayStatusDto> getPlaysUser(ServletRequest request) {
//        return userStatusService.getPlayStatus(getEmail(request));
//    }

    @GetMapping("/studys")
    public List<StudyStatusDto> getMypagePlaysUser(ServletRequest request) {
        return userStatusService.getStudyStatus(getEmail(request));
    }

    private String getEmail(ServletRequest request){
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        return jwtTokenProvider.getUserPk(token);
    }
}
