package com.example.b4.service.user;

import com.example.b4.dto.user.PlayStatusDto;
import com.example.b4.entity.User;
import com.example.b4.repository.UserRepository;
import com.example.b4.repository.mind.MindRepository;
import com.example.b4.repository.play.PlayRepository;
import com.example.b4.repository.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserStatusService {

    private final PlayRepository playRepository;
    private final MindRepository mindRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

//    public List<PlayStatusDto> getPlayStatus(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new IllegalStateException("없는 유저"));
//        List<PlayStatusDto> playStatusDtoList = new ArrayList<>();
//
//    }
}
