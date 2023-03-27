package com.example.b4.repository.study;

import com.example.b4.dto.study.StudyDetailDto;
import com.example.b4.dto.study.StudyListDto;
import com.example.b4.entity.post.Post;
import com.example.b4.entity.post.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {
//    @Query("select new com.example.b4.dto.study.StudyListDto(" +
//            "p.postId,p.user.nickname,p.title,p.category,p.attachedFile,p.bookmark,p.createdDate)" +
//            "from Study s join s.post p where p.postId=s.post.postId")
//    List<StudyListDto> findStudyListDto();

    Study findByPost(Post post);

    @Query("select new com.example.b4.dto.study.StudyListDto(" +
            "s.studyId,p.user.nickname,p.title,p.category,p.attachedFile,p.bookmark,p.createdDate,p.comments.size)" +
            "from Study s join s.post p where p.postId=s.post.postId ")
    List<StudyListDto> findStudyListDto();

    @Query("select new com.example.b4.dto.study.StudyListDto(" +
            "s.studyId,p.user.nickname,p.title,p.category,p.attachedFile,p.bookmark,p.createdDate,p.comments.size)" +
            "from Study s join s.post p where p.category = :category and p.postId = s.post.postId  ")
    List<StudyListDto> findStudyCategoryListDto(@Param("category") String category);

    @Query("select new com.example.b4.dto.study.StudyDetailDto(" +
            "p.category,p.title,p.category,s.studyDetails,p.attachedFile,p.createdDate)" +
            "from Study s join s.post p where p.postId=s.post.postId")
    List<StudyDetailDto> findStudyDetailDto();

    @Query("select new com.example.b4.dto.study.StudyDetailDto(" +
            "p.category,p.title,p.user.nickname,s.studyDetails,p.attachedFile,p.createdDate)" +
            "from Study s join s.post p where p.postId=:postId and p.postId=s.post.postId")
    StudyDetailDto findByPostIdDetailDto(@Param("postId")Long postId);


    @Query("select p from Study s join s.post p where s.studyId =:studyId and s.post.postId = p.postId")
    Post findPostByStudy(@Param("studyId")Long studyId);
}