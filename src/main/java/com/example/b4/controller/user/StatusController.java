package com.example.b4.controller.user;

import com.example.b4.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatusController {
    private final JwtTokenProvider jwtTokenProvider;
}
