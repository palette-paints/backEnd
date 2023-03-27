package com.example.b4.repository.play;

import com.example.b4.dto.play.PlayDetailDto;
import com.example.b4.dto.play.PlayListDto;
import com.example.b4.entity.post.Play;
import com.example.b4.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayRepository extends JpaRepository<Play,Long> {

    Play findByPost(Post post);

    @Query("select new com.example.b4.dto.play.PlayListDto(" +
            "pl.playId,p.user.nickname,p.title,p.category,p.attachedFile,p.bookmark,p.createdDate,pl.status)" +
            "from Play pl join pl.post p where p.postId=pl.post.postId")
    List<PlayListDto> findPlayListDto();

    @Query("select new com.example.b4.dto.play.PlayListDto(" +
            "pl.playId,p.user.nickname,p.title,p.category,p.attachedFile,p.bookmark,p.createdDate,pl.status)" +
            "from Play pl join pl.post p where p.category =: category and p.postId=pl.post.postId")
    List<PlayListDto> findPlayCategoryListDto(@Param("category") String category);

    @Query("select new com.example.b4.dto.play.PlayDetailDto(" +
            "p.category,p.title,p.user.nickname,pl.playDetails,p.attachedFile,p.createdDate,pl.submitLink,pl.dueTo,pl.status)" +
            "from Play pl join pl.post p where p.postId=:postId and p.postId=pl.post.postId")
    PlayDetailDto findByPostIdDetailDto(@Param("postId") Long postId);

    @Query("select p from Play pl join pl.post p where pl.playId=:playId and pl.post.postId = p.postId")
    Post findPostByPlay(@Param("playId") Long playId);
}

