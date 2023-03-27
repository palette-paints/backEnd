package com.example.b4.service.user;

import com.example.b4.dto.user.MindStatusDto;
import com.example.b4.dto.user.PlayStatusDto;
import com.example.b4.dto.user.StudyStatusDto;
import com.example.b4.entity.User;
import com.example.b4.entity.post.*;
import com.example.b4.repository.PostRepository;
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

    private final PostRepository postRepository;
    private final PlayRepository playRepository;
    private final MindRepository mindRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    public List<PlayStatusDto> getPlayStatus(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("없는 유저"));
        List<PlayStatusDto> plays = new ArrayList<>();
        List<Post> posts = postRepository.findByCodeAndUser(PostCategory.PLAY, user);

        for(Post post : posts) {
            Play p = playRepository.findByPost(post);
            PlayStatusDto play = new PlayStatusDto(post);
            play.updateUserAndStatus(user, p.getStatus());
            plays.add(play);
        }

        return  plays;
    }

    public List<StudyStatusDto> getStudyStatus(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("없는 유저"));
        List<StudyStatusDto> studys = new ArrayList<>();
        List<Post> posts = postRepository.findByCodeAndUser(PostCategory.STUDY, user);

        for(Post post : posts) {
            Study s = studyRepository.findByPost(post);
            StudyStatusDto study = new StudyStatusDto(post);
            study.updateUser(user);
            studys.add(study);
        }

        return studys;
    }

    public List<MindStatusDto> getMindStatus(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("없는 유저"));
        List<MindStatusDto> minds = new ArrayList<>();
        List<Post> posts = postRepository.findByCodeAndUser(PostCategory.MIND, user);

        for(Post post : posts) {
            Mind m = mindRepository.findByPost(post);
            MindStatusDto mind = new MindStatusDto(post);
            mind.updateUserStatus(user,m);
            minds.add(mind);
        }

        return minds;
    }
}
