package com.example.b4.service.play;

import com.example.b4.dto.play.PlayDetailDto;
import com.example.b4.dto.play.PlayDetailReq;
import com.example.b4.dto.play.PlayListDto;
import com.example.b4.dto.study.StudyDetailDto;
import com.example.b4.entity.User;
import com.example.b4.entity.post.Play;
import com.example.b4.entity.post.Post;
import com.example.b4.entity.post.PostCategory;
import com.example.b4.repository.PostRepository;
import com.example.b4.repository.UserRepository;
import com.example.b4.repository.play.PlayRepository;
import com.example.b4.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class PlayService {

    private final PostRepository postRepository;
    private final PlayRepository playRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    public PlayDetailDto createPlay(PlayDetailReq req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("회원 없음"));;

        Post newPost = Post.builder()
                .bookmark(Boolean.FALSE)
                .user(user)
                .title(req.getTitle())
                .attachedFile(req.getPlayAttachedFile())
                .category(req.getCategory())
                .code(PostCategory.PLAY)
                .build();

        Play newPlay = Play.builder()
                .post(newPost)
                .playDetails(req.getPlayDetails())
                .dueTo(LocalDateTime.now())
                .submitLink(req.getSubmitLink())
                .status("검토중")
                .build();
        Play savedPlay = playRepository.save(newPlay);

        return new PlayDetailDto(savedPlay.getPost().getCategory(), savedPlay.getPost().getTitle(),
                savedPlay.getPost().getUser().getNickname(),
                savedPlay.getPlayDetails(), savedPlay.getPost().getAttachedFile(), savedPlay.getPost().getCreatedDate(), savedPlay.getSubmitLink(), savedPlay.getDueTo(), savedPlay.getStatus());
    }

    public PlayDetailDto getPlayDetailsByPlayId(Long playId) {
        Post findPost = playRepository.findPostByPlay(playId);
        Long postId = findPost.getPostId();

        return playRepository.findByPostIdDetailDto(postId);
    }

    public PlayDetailDto updatePlay(Long playId, PlayDetailReq req, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("회원 없음"));;

        Optional<Play> optional = playRepository.findById(playId);
        Play updatePlay = optional.get();
        updatePlay.updatePlayDetails(req.getPlayDetails());
        updatePlay.updateDueTo(LocalDateTime.now());
        updatePlay.updateSubmitLink(req.getSubmitLink());
        Play savedPlay = playRepository.save(updatePlay);

        Post updatePost = playRepository.findPostByPlay(playId);
        updatePost.updateCategory(req.getCategory());
        updatePost.updateTitle(req.getTitle());
        updatePost.updateAttachedFile(req.getPlayAttachedFile());
        Post savedPost = postRepository.save(updatePost);

        return new PlayDetailDto(savedPlay.getPost().getCategory(), savedPlay.getPost().getTitle(),
                savedPlay.getPost().getUser().getNickname(),
                savedPlay.getPlayDetails(), savedPlay.getPost().getAttachedFile(), savedPlay.getPost().getCreatedDate(), savedPlay.getSubmitLink(), savedPlay.getDueTo(), savedPlay.getStatus());
    }

    public void deletePlay(Long playId) {
        Play findPlay = playRepository.findById(playId).get();
        Post findPost = postRepository.findById(findPlay.getPost().getPostId()).get();

        postRepository.delete(findPost);
        playRepository.delete(findPlay);
    }

    public List<PlayListDto> getPlayLists() {
        return playRepository.findPlayListDto();
    }
    public List<PlayListDto> getPlayListsByCategory(String category) {
        return playRepository.findPlayCategoryListDto(category);
    }

    public String bookmark(Long playId, String bookmark) {

        Post findPost = playRepository.findPostByPlay(playId);

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

