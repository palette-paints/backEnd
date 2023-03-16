package com.example.b4.service.user;

import com.example.b4.dto.user.MypageDto;
import com.example.b4.entity.User;
import com.example.b4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MypageService {
    private final UserRepository userRepository;

    public MypageDto getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("없는 유저"));

        MypageDto mypageDto = new MypageDto(user);

        return mypageDto;
    }

    public ResponseEntity updateUserInfo(String email, MypageDto mypageDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("없는 유저"));

        user.updateUserInfo(mypageDto);
        userRepository.save(user);

        return new ResponseEntity("유저 정보 업데이트 완료", HttpStatus.OK);
    }
}
