package com.example.b4.repository;

import com.example.b4.dto.study.StudyDetailDto;
import com.example.b4.dto.study.StudyListDto;
import com.example.b4.entity.User;
import com.example.b4.entity.post.Post;
import com.example.b4.entity.post.PostCategory;
import com.example.b4.entity.post.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {


    Post findByPostId(Long postId);

    List<Post> findByCodeAndUser(PostCategory postCategory, User user);

    @Query("select new com.example.b4.dto.study.StudyListDto(" +
            "p.postId,p.title,p.category,p.attachedFile,p.bookmark)" +
            "from Post p , Study s where p.postId=s.post.postId")
    List<StudyListDto> findStudyListDto();

    @Query("select new com.example.b4.dto.study.StudyDetailDto(" +
            "p.category,p.title,p.category,s.studyDetails,p.attachedFile,p.createdDate)" +
            "from Post p, Study s where p.postId=s.post.postId")
    List<StudyDetailDto> findStudyDetailDto();

//    @Query("select p from Post p where p=Study.post")
//    List<Post> findStudy();

}
