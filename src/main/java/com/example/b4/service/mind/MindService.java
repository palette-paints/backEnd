package com.example.b4.service.mind;


import com.example.b4.dto.mind.MindDetailDto;
import com.example.b4.dto.mind.MindDetailReq;
import com.example.b4.dto.mind.MindListDto;
import com.example.b4.entity.User;
import com.example.b4.entity.post.Mind;
import com.example.b4.entity.post.Post;
import com.example.b4.entity.post.PostCategory;
import com.example.b4.repository.PostRepository;
import com.example.b4.repository.UserRepository;
import com.example.b4.repository.mind.MindRepository;
import com.example.b4.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MindService {

    private final PostRepository postRepository;
    private final MindRepository mindRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    public MindDetailDto createMind(MindDetailReq req, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("회원 없음"));;

        Post newPost = Post.builder()
                .bookmark(Boolean.FALSE)
                .user(user)
                .title(req.getTitle())
                .attachedFile(req.getMindAttachedFile())
                .category(req.getCategory())
                .code(PostCategory.MIND)
                .build();

        Boolean status = Boolean.FALSE;
        if(req.getStatus().equals("O")){
            status = Boolean.TRUE;
        }

        Mind newMind = Mind.builder()
                .post(newPost)
                .mindDetails(req.getMindDetails())
                .status(status)
                .build();
        Mind savedMind = mindRepository.save(newMind);
        return new MindDetailDto(savedMind.getPost().getCategory(), savedMind.getPost().getTitle(),
                savedMind.getPost().getUser().getNickname(), savedMind.getMindDetails(),
                savedMind.getPost().getAttachedFile(), savedMind.getPost().getCreatedDate(), savedMind.getStatus(),
                savedMind.getPassword());

    }

    public MindDetailDto getMindDetailsByMindId(Long mindId) {
        Post findPost = mindRepository.findPostByMind(mindId);
        Long postId = findPost.getPostId();

        MindDetailDto getMindDetail = mindRepository.findByPostIdDetailDto(postId);
        getMindDetail.setComments(commentService.readComments(postId));
        return getMindDetail;
    }

    public MindDetailDto updateMind(Long mindId, MindDetailReq req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("회원 없음"));;

        Optional<Mind> optional = mindRepository.findById(mindId);
        Mind updateMind = optional.get();
        updateMind.updateMindDetails(req.getMindDetails());

        Boolean status = Boolean.FALSE;
        if(req.getStatus().equals("O")){
            status = Boolean.TRUE;
        }
        updateMind.updateStatus(status);
        updateMind.updatePassword(req.getPassword());
        Mind savedMind = mindRepository.save(updateMind);

        Post updatePost = mindRepository.findPostByMind(mindId);
        updatePost.updateTitle(req.getTitle());
        updatePost.updateAttachedFile(req.getMindAttachedFile());
        updatePost.updateCategory(req.getCategory());
        Post savedPost = postRepository.save(updatePost);

        return new MindDetailDto(savedPost.getCategory(), savedPost.getTitle(), savedPost.getUser().getNickname(),
                savedMind.getMindDetails(), savedPost.getAttachedFile(), savedPost.getCreatedDate(),
                savedMind.getStatus(), savedMind.getPassword());
    }

    public void deleteMind(Long mindId) {
        Mind findMind = mindRepository.findById(mindId).get();
        Post findPost = mindRepository.findPostByMind(mindId);
        postRepository.delete(findPost);
        mindRepository.delete(findMind);

    }

    public List<MindListDto> getMindLists() {
        return mindRepository.findMindListDto();
    }

    public List<MindListDto> getMindListsByCategory(String category) {
        return mindRepository.findMindCategoryListDto(category);
    }

    public String bookmark(Long mindId, String bookmark) {

        Post findPost = mindRepository.findPostByMind(mindId);

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
