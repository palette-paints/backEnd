package com.example.b4.dto.user;

import com.example.b4.entity.User;
import com.example.b4.entity.post.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudyStatusDto {
    private String title;
    private String user;
    private String category;
    private LocalDateTime createdAt;
    private String studyAttachedFile;
    private Boolean bookmark;

    public StudyStatusDto (Post post) {
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.createdAt = post.getCreatedDate();
        this.studyAttachedFile = post.getAttachedFile();
        this.bookmark = post.getBookmark();
    }

    public void updateUser(User user) {
        this.user = user.getUsername();
    }
}

