package com.example.b4.service.study;

import com.example.b4.dto.study.StudyDetailDto;
import com.example.b4.dto.study.StudyDetailReq;
import com.example.b4.dto.study.StudyListDto;
import com.example.b4.entity.post.Post;
import com.example.b4.entity.post.Study;
import com.example.b4.entity.user.User;
import com.example.b4.repository.PostRepository;
import com.example.b4.repository.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final PostRepository postRepository;
    private final StudyRepository studyRepository;

    public StudyDetailDto createStudy(StudyDetailReq req) {
//        User user = new User();
//        user.setUserId(1L);
//        user.setUserNickname("user1");

        Post newPost = Post.builder()
                .bookmark(Boolean.TRUE)
                .user(null)
                .title(req.getTitle())
                .attachedFile(req.getStudyAttachedFile())
                .category(req.getCategory())
                .build();
        Post savedPost = postRepository.save(newPost);

        Study newStudy = Study.builder()
                .post(savedPost)
                .studyDetails(req.getStudyDetails())
                .build();
        Study savedStudy = studyRepository.save(newStudy);
        return new StudyDetailDto(savedPost.getCategory(), savedPost.getTitle(), "user1", savedStudy.getStudyDetails(), savedPost.getAttachedFile());

    }

    public List<Post> getAllStudy() {
        return postRepository.findAll();
    }

    public List<StudyListDto> getStudyLists(){
        return postRepository.findStudyListDto();
    }

    public List<Post> getStudy() {
        return null;
//        return postRepository.findStudy();
    }






}
