package com.example.b4.service.study;

import com.example.b4.dto.study.StudyDetailDto;
import com.example.b4.dto.study.StudyDetailReq;
import com.example.b4.dto.study.StudyListDto;
import com.example.b4.entity.User;
import com.example.b4.entity.post.Post;
import com.example.b4.entity.post.PostCategory;
import com.example.b4.entity.post.Study;
//import com.example.b4.entity.user.User;
import com.example.b4.repository.PostRepository;
import com.example.b4.repository.UserRepository;
import com.example.b4.repository.comment.CommentRepository;
import com.example.b4.repository.study.StudyRepository;
import com.example.b4.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Transactional

@Service
@RequiredArgsConstructor
public class StudyService {

    private final PostRepository postRepository;
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    public StudyDetailDto createStudy(StudyDetailReq req, Long userId) {
//            user.setUserId(1L);
//            user.setUserNickname("user1");
        // TODO : JWT로 교체
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("회원 없음"));;
//        User user;


        Post newPost = Post.builder()
                .bookmark(Boolean.FALSE)
                .user(user)
                .title(req.getTitle())
                .attachedFile(req.getStudyAttachedFile())
                .category(req.getCategory())
                .code(PostCategory.STUDY)
                .build();
//        Post savedPost = postRepository.save(newPost);

        Study newStudy = Study.builder()
                .post(newPost)
                .studyDetails(req.getStudyDetails())
                .build();
        Study savedStudy = studyRepository.save(newStudy);
//        return new StudyDetailDto(savedPost.getCategory(), savedPost.getTitle(), savedPost.getUser().getUserNickname(),savedStudy.getStudyDetails(), savedPost.getAttachedFile(),savedPost.getCreatedDate());
        return new StudyDetailDto(savedStudy.getPost().getCategory(), savedStudy.getPost().getTitle(), savedStudy.getPost().getUser().getNickname(),savedStudy.getStudyDetails(), savedStudy.getPost().getAttachedFile(),savedStudy.getPost().getCreatedDate());

    }

    public StudyDetailDto getStudyDetailByStudyId(Long studyId) {

        Post findPost = studyRepository.findPostByStudy(studyId);
        Long postId = findPost.getPostId();

        StudyDetailDto getStudyDetail = studyRepository.findByPostIdDetailDto(postId);
        getStudyDetail.setComments(commentService.readComments(postId));
        return getStudyDetail;
    }

    public StudyDetailDto updateStudy(Long studyId,StudyDetailReq req, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("회원 없음"));;

        Optional<Study> optional = studyRepository.findById(studyId);

        Study updateStudy = optional.get();
        updateStudy.updateStudyDetails(req.getStudyDetails());
        Study savedStudy = studyRepository.save(updateStudy);

        Post updatePost = studyRepository.findPostByStudy(studyId);
        updatePost.updateAttachedFile(req.getStudyAttachedFile());
        updatePost.updateCategory(req.getCategory());
        updatePost.updateTitle(req.getTitle());
        Post savedPost = postRepository.save(updatePost);

        //TODO : pleaseUpdate

        return new StudyDetailDto(savedPost.getCategory(), savedPost.getTitle(), savedPost.getUser().getNickname(),savedStudy.getStudyDetails(), savedPost.getAttachedFile(),savedPost.getCreatedDate());
    }

    public void deleteStudy(Long studyId) {
        Study findStudy = studyRepository.findById(studyId).get();
        Post findPost = postRepository.findById(findStudy.getPost().getPostId()).get();
        postRepository.delete(findPost);
        studyRepository.delete(findStudy);

    }

    // return All Post
    public List<Post> getAllStudy() {
        return postRepository.findAll();
    }

    // return All StudyListDto
    public List<StudyListDto> getStudyLists(){
        return studyRepository.findStudyListDto();
    }

    // return category StudyListDto
    public List<StudyListDto> getStudyListsByCategory(String category) {
        return studyRepository.findStudyCategoryListDto(category);
    }

    // return StudyDetailDto
    public List<StudyDetailDto> getStudyDetail() {
        return studyRepository.findStudyDetailDto();
    }
    // return

    public String bookmark(Long studyId, String bookmark) {

        Post findPost = studyRepository.findPostByStudy(studyId);

        if (bookmark.equals("O")) {
            findPost.updateBookmark(Boolean.TRUE);
            Post savedPost = postRepository.save(findPost);
            return savedPost.getBookmark().toString();
        }else{
            findPost.updateBookmark(Boolean.FALSE);
            Post savedPost = postRepository.save(findPost);
            return savedPost.getBookmark().toString();

        }

    }





}