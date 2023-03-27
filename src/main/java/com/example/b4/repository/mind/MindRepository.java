package com.example.b4.repository.mind;

import com.example.b4.dto.mind.MindDetailDto;
import com.example.b4.dto.mind.MindListDto;
import com.example.b4.dto.study.StudyDetailDto;
import com.example.b4.dto.study.StudyListDto;
import com.example.b4.entity.post.Mind;
import com.example.b4.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MindRepository extends JpaRepository<Mind, Long> {

    Mind findByPost(Post post);

    @Query("select new com.example.b4.dto.mind.MindListDto(" +
            "m.mindId,p.user.nickname,p.title,p.category,p.attachedFile,p.bookmark,m.status,p.comments.size,p.createdDate)" +
            "from Mind m join m.post p where p.postId=m.post.postId ")
    List<MindListDto> findMindListDto();

    @Query("select new com.example.b4.dto.mind.MindListDto(" +
            "m.mindId,p.user.nickname,p.title,p.category,p.attachedFile,p.bookmark,m.status,p.comments.size,p.createdDate)" +
            "from Mind m join m.post p where p.category = :category and p.postId = m.post.postId ")
    List<MindListDto> findMindCategoryListDto(@Param("category") String category);

    @Query("select new com.example.b4.dto.mind.MindDetailDto(" +
            "p.category,p.title,p.user.nickname,m.mindDetails,p.attachedFile,p.createdDate,m.status,m.password)" +
            "from Mind m join m.post p where p.postId=m.post.postId")
    List<MindDetailDto> findMindDetailDto();

    @Query("select new com.example.b4.dto.mind.MindDetailDto(" +
            "p.category,p.title,p.user.nickname,m.mindDetails,p.attachedFile,p.createdDate,m.status,m.password)" +
            "from Mind m join m.post p where p.postId=:postId and p.postId=m.post.postId")
    MindDetailDto findByPostIdDetailDto(@Param("postId")Long postId);


    @Query("select p from Mind m join m.post p where m.mindId =:mindId and m.post.postId = p.postId")
    Post findPostByMind(@Param("mindId")Long mindId);
}
